@file:Suppress("MemberVisibilityCanBePrivate")

package com.kotlingang.kds

import com.kotlingang.kds.builder.StorageConfig
import com.kotlingang.kds.delegate.PropertyDelegate
import com.kotlingang.kds.storage.BaseStorage
import com.kotlingang.kds.storage.dirPath
import com.kotlingang.kds.storage.joinPath
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    private val defaultPath = dirPath.joinPath("data")
    private val config = StorageConfig(defaultPath).apply(builder)

    private val path = config.path ?: defaultPath.joinPath(this::class.getDefaultFilename() + ".json")
    val json = config.json

    /* Internal Storage API */
    private val baseStorage = BaseStorage(path)

    // Store for value references. Useful is value is mutable to save it later
    private val referencesSource: MutableMap<String, Pair<Any?, KSerializer<*>>> = mutableMapOf()
    internal fun <T> saveReference(name: String, value: T, serializer: KSerializer<T>) {
        referencesSource[name] = value to serializer
    }
    internal fun getReference(name: String) = referencesSource[name]?.component1()

    private var dataSource: MutableMap<String, JsonElement>? = null
    internal val data get() = runBlocking { awaitLoading() }.let {
        dataSource ?: error("Internal error, because storage is not loaded. " +
            "You shouldn't see this error, please create an issue. To fix it try awaitLoading before using storage")
    }

    private val mutex = Mutex()
    private var savingDeferred: Deferred<Unit>? = null
    /**
     * Prevents redundant operations when it called one by one.
     * [runBlocking] used there because there is no heavy operations, just thread-safety
     */
    private fun privateCommitAsync(): Deferred<Unit> = runBlocking {
        mutex.withLock {
            savingDeferred?.cancel()
            savingDeferred = async {
                saveReferencesToData()
                baseStorage.saveStorage(Json.encodeToString(data))
            }
            savingDeferred ?: error("Unreachable error")
        }
    }

    /**
     * Encodes all values from [referencesSource] to JsonElement and puts it to [data]
     */
    private suspend fun saveReferencesToData() {
        mutex.withLock {
            for((name, pair) in referencesSource) {
                val (value, serializer) = pair
                @Suppress("UNCHECKED_CAST")
                fun <T> uncheckedSet(serializer: KSerializer<T>) {
                    data[name] = Json.encodeToJsonElement(serializer, value as T)
                }
                uncheckedSet(serializer)
            }
        }
    }

    override val coroutineContext: CoroutineContext = GlobalScope.coroutineContext + Job()

    /* ----- */

    fun <T> property(serializer: KSerializer<T>, default: T) = PropertyDelegate(serializer, default)
    inline fun <reified T> property(default: T) = property(json.serializersModule.serializer(), default)

    private val loadingDeferred = async {
        dataSource = Json.decodeFromString(baseStorage.loadStorage() ?: "{}")
    }
    /**
     * Await first loading; Should be done before storage usage
     */
    suspend fun awaitLoading() = loadingDeferred.await()

    /**
     *  Call it if mutable data was changed to commit data async
     */
    fun launchCommit() = launch { privateCommitAsync().await() }

    /**
     * Call it if mutable data was changed to commit data sync
     */
    suspend fun commit() = launchCommit().join()

    /**
     * Should be done at the end of storage usage (e.g. in console apps before it closes; useless in android)
     */
    suspend fun awaitLastCommit() = savingDeferred?.join().unit
}


private fun KClass<*>.getDefaultFilename() = simpleName ?: "noname"
