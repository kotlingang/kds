package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.InternalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.file.CommonFile
import `fun`.kotlingang.kds.storage.AsyncCommittableStorage
import `fun`.kotlingang.kds.storage.JsonElementDataStorage
import `fun`.kotlingang.kds.sync.platformSynchronized
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


internal class JsonElementFileDataStorage (
    private val json: Json,
    private val file: CommonFile,
    private val scope: CoroutineScope
) : JsonElementDataStorage, AsyncCommittableStorage {
    init {
        if(!file.exists) {
            file.parentFile?.mkdir(recursive = true)
            file.createNewFile(defaultText = "{}")
        }
    }

    private val asyncData = scope.async { file.readText() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val data by lazy {
        val text = if(asyncData.isCompleted) {
            asyncData.getCompleted()
        } else {
            file.readTextBlocking()
        }
        json.decodeFromString<Map<String, JsonElement>>(text).toMutableMap()
    }

    @OptIn(RawSetterGetter::class)
    override fun putJsonElement(key: String, value: JsonElement) {
        data[key] = value
    }

    @OptIn(RawSetterGetter::class)
    override fun getJsonElement(key: String): JsonElement? = data[key]

    override fun setupBlocking() {
        // Lazy invocation
        data
    }

    override suspend fun setup() {
        asyncData.await()
    }

    @OptIn(InternalKDSApi::class)
    private fun encodeData() = json.encodeToString (
        platformSynchronized(lock = this) { data.toMap() }
    )

    override fun commitBlocking() = file.writeTextBlocking(encodeData())

    private val commitMutex = Mutex()
    override suspend fun commit() = commitMutex.withLock {
        file.writeText(encodeData())
    }

    private val commitJobMutex = Mutex()
    private var commitJob: Job? = null
    override fun launchCommit() {
        scope.launch {
            commitJobMutex.withLock {
                commitJob?.cancelAndJoin()
                commitJob = scope.launch { commit() }
            }
        }
    }
}
