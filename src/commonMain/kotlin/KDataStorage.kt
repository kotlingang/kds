@file:Suppress("MemberVisibilityCanBePrivate")

package com.kotlingang.kds

import com.kotlingang.kds.builder.StorageConfig
import com.kotlingang.kds.delegate.KDataStorageProperty
import com.kotlingang.kds.storage.BaseStorage
import com.kotlingang.kds.storage.dirPath
import com.kotlingang.kds.storage.joinPath
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass


typealias StorageConfigBuilder = StorageConfig.() -> Unit

/**
 * If you are using mutable objects, do not forget to use [KDataStorage.apply] (async) or [KDataStorage.commit] (sync) to save current state (if you are using immutable objects)
 */
open class KDataStorage(
        builder: StorageConfigBuilder = {}
) : CoroutineScope {
    constructor(name: String, builder: StorageConfigBuilder = {}) : this({
        name(name)
        builder()
    })

    internal val blockingLocker = PlatformLocker()

    private val defaultPath = dirPath.joinPath("data")
    private val config = StorageConfig(defaultPath).apply(builder)

    private val path = config.path ?: defaultPath.joinPath(this::class.getDefaultFilename() + ".json")
    val json = config.json

    /* Internal Storage API */
    private val baseStorage = BaseStorage(path)

    // Store for value references. Used to save mutable objects
    private val referencesSource: MutableMap<String, Pair<Any?, KSerializer<*>>> = mutableMapOf()
    internal fun <T> saveReference(name: String, value: T, serializer: KSerializer<T>) {
        referencesSource[name] = value to serializer
    }
    internal fun getReference(name: String): Pair<Boolean, Any?> {
        val reference = referencesSource[name]
        return (reference != null) to reference?.component1()
    }

    // Store for encoded values
    private var dataSource: MutableMap<String, JsonElement>? = null
    // When get, try to await it blocking or error 
    internal val data get() = runBlockingPlatform { awaitLoading() }.let { (setup) ->
        dataSource ?: if(setup) {
            error("Internal error, because storage is not loaded. " +
                    "You shouldn't see this error, please create an issue. " +
                    "To fix it try awaitLoading before using storage")
        } else error("Your target (js) cannot setup storage blocking. Please call awaitLoading() first")
    }
    
    private var savingJob: Job? = null
    /**
     * Prevents redundant operations when it called one by one.
     * [blockingLocker] used there because there is no heavy operations, just thread-safety
     */
    private fun privateLaunchCommit() = launch {
        blockingLocker.withLock {
            savingJob?.cancel()
            return@withLock launch {
                saveReferencesToData()
                // to prevent concurrent modification exception
                val json = blockingLocker.withLock { json.encodeToString(data) }
                baseStorage.saveStorage(json)
            }.also { job ->
                savingJob = job
            }
        }.join()
    }

    /**
     * Encodes all values from [referencesSource] to JsonElement and puts it to [data].
     * So state of mutable objects will be saved
     */
    private fun saveReferencesToData() {
        blockingLocker.withLock {
            for((name, pair) in referencesSource) {
                val (value, serializer) = pair
                @Suppress("UNCHECKED_CAST")
                fun <T> uncheckedSet(serializer: KSerializer<T>) {
                    data[name] = json.encodeToJsonElement(serializer, value as T)
                }
                uncheckedSet(serializer)
            }
        }
    }

    override val coroutineContext: CoroutineContext = GlobalScope.coroutineContext + Job()

    /* ----- */

    fun <T> property(serializer: KSerializer<T>, lazyDefault: () -> T) = KDataStorageProperty(serializer, lazyDefault)
    inline fun <reified T> property(noinline lazyDefault: () -> T) = property<T>(json.serializersModule.serializer(), lazyDefault)

    fun <T> property(serializer: KSerializer<T>, default: T) = property<T>(serializer) { default }
    inline fun <reified T> property(default: T) = property(json.serializersModule.serializer(), default)

    fun <T> property(serializer: KSerializer<T?>) = property(serializer, default = null)
    inline fun <reified T> property() = property<T?>(json.serializersModule.serializer())

    private val loadingDeferred = async {
        dataSource = json.decodeFromString(baseStorage.loadStorage() ?: "{}")
    }
    /**
     * Await first loading; Should be done before storage usage
     */
    suspend fun awaitLoading() = loadingDeferred.await()

    /**
     *  Call it if mutable data was changed to commit data async
     */
    fun launchCommit() = privateLaunchCommit()

    /**
     * Call it if mutable data was changed to commit data sync
     */
    suspend fun commit() = privateLaunchCommit().join()

    /**
     * Clear property value
     */
    fun clear(propertyName: String) {
        blockingLocker.withLock {
            referencesSource.remove(propertyName)
            data.remove(propertyName)
        }
        launchCommit()
    }

    /**
     * Should be done at the end of storage usage (e.g. in console apps before it closes; useless in android)
     */
    suspend fun awaitLastCommit() = savingJob?.join().unit
}


/**
 * Edit mutable values inside block
 */
inline fun <T : KDataStorage> T.mutate(block: T.() -> Unit) {
    block()
    launchCommit()
}

private fun KClass<*>.getDefaultFilename() = simpleName ?: "noname"
