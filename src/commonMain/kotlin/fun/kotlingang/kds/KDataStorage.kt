@file:Suppress("MemberVisibilityCanBePrivate")

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.builder.StorageConfig
import `fun`.kotlingang.kds.delegate.KDataStorageProperty
import `fun`.kotlingang.kds.storage.BaseStorage
import `fun`.kotlingang.kds.storage.dirPath
import `fun`.kotlingang.kds.storage.joinPath
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
 * If you are using mutable objects, do not forget to use [KDataStorage.mutate]
 */
open class KDataStorage (
    /**
     * Scope there used only for cases when program finishes nut there are still some jobs launched
     */
    coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope + SupervisorJob(),
    builder: StorageConfigBuilder = {}
)  {
    constructor (
        name: String,
        coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope,
        builder: StorageConfigBuilder = {}
    ) : this (
        coroutineScope, { name(name); builder() }
    )

    private val scope = coroutineScope + Job() + CoroutineName(name = "KDS Coroutine")

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
    // Mutable value required in Javascript, so there is no way to run await blocking
    private var dataSource: MutableMap<String, JsonElement>? = null

    private val dataLoadingJob = scope.launch {
        dataSource = json.decodeFromString(string = baseStorage.loadStorage() ?: "{}")
    }

    // When get, try to await it blocking or error 
    internal val data get() = dataSource ?: runBlockingPlatform { dataLoadingJob.join() }.let { (setup, _) ->
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
    private fun privateLaunchCommit() = scope.launch {
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

    /* ----- */

    fun <T> property(serializer: KSerializer<T>, lazyDefault: () -> T) = KDataStorageProperty(serializer, lazyDefault)
    inline fun <reified T> property(noinline lazyDefault: () -> T) = property<T>(json.serializersModule.serializer(), lazyDefault)

    fun <T> property(serializer: KSerializer<T>, default: T) = property<T>(serializer) { default }
    inline fun <reified T> property(default: T) = property(json.serializersModule.serializer(), default)

    fun <T> property(serializer: KSerializer<T?>) = property(serializer, default = null)
    inline fun <reified T> property() = property<T?>(json.serializersModule.serializer())

    /**
     * Await first loading; Should be done before fun.kotlingang.kds.storage usage
     */
    suspend fun awaitLoading() = dataLoadingJob.join()

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
}


/**
 * Edit mutable values inside block
 */
inline fun <T : KDataStorage> T.mutate(block: T.() -> Unit) {
    block()
    launchCommit()
}

/**
 * Like [mutate] but commit is awaited
 */
suspend inline fun <T : KDataStorage> T.commitMutate(block: T.() -> Unit) {
    awaitLoading()
    block()
    commit()
}

private fun KClass<*>.getDefaultFilename() = simpleName ?: "noname"
