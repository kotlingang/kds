package com.kotlingang.kds

import com.kotlingang.kds.builder.StorageConfig
import com.kotlingang.kds.delegate.PropertyDelegate
import com.kotlingang.kds.storage.BaseStorage
import com.kotlingang.kds.storage.dirPath
import com.kotlingang.kds.storage.joinPath
import com.kotlingang.kds.utils.runBlocking
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass


typealias StorageConfigBuilder = StorageConfig.() -> Unit

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

    private var dataSource: MutableMap<String, JsonElement>? = null
    internal val data get() = runBlocking {
        awaitLoading()
    }.let {
        dataSource ?: error("Internal error, because storage is not loaded. " +
            "You shouldn't see this error, please create an issue. To fix it try awaitLoading before using storage")
    }

    private var savingDeferred: Deferred<Unit>? = null
    /**
     * Prevents redundant operations when it called one by one
     */
    internal fun saveStorageAsync() {
        savingDeferred?.cancel()
        savingDeferred = async {
            baseStorage.saveStorage(Json.encodeToString(data))
        }
    }

    override val coroutineContext: CoroutineContext = GlobalScope.coroutineContext + Job()

    /* ----- */

    /**
     * NOTE THAT YOU SHOULD USE IMMUTABLE OBJECTS ONLY!
     * KDataStorage CANNOT OBSERVE MUTABLE OBJECTS' CHANGES!
     *
     * Checkout README for example with mutable objects
     */
    fun <T> property(serializer: KSerializer<T>, default: T) = PropertyDelegate(serializer, default)
    inline fun <reified T> property(default: T) = property(json.serializersModule.serializer(), default)

    private val loadingDeferred = async {
        dataSource = Json.decodeFromString(baseStorage.loadStorage() ?: "{}")
    }
    /**
     * Await first loading; Must be done before storage usage
     */
    suspend fun awaitLoading() = loadingDeferred.await()

    /**
     * Should be done at the end of storage usage (e.g. in console apps before it closes; useless in android)
     */
    suspend fun awaitSaving() = savingDeferred?.join()
}


private fun KClass<*>.getDefaultFilename() = simpleName ?: "noname"
