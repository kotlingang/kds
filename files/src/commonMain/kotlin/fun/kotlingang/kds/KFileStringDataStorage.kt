@file:OptIn(DelicateCoroutinesApi::class)

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.InternalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.file.CommonFile
import `fun`.kotlingang.kds.storage.AsyncCommittableStorage
import `fun`.kotlingang.kds.storage.CommittableStorage
import `fun`.kotlingang.kds.storage.StringDataStorage
import `fun`.kotlingang.kds.sync.platformSynchronized
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


@OptIn(InternalKDSApi::class)
class KFileStringDataStorage (
    private val file: CommonFile,
    private val scope: CoroutineScope,
    private val dataTransformer: DataTransformer
) : StringDataStorage, AsyncCommittableStorage {

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
        dataTransformer.decode(text).toMutableMap()
    }

    @RawSetterGetter
    override fun putString(key: String, value: String) {
        platformSynchronized(lock = this) {
            data[key] = value
        }
    }

    @RawSetterGetter
    override fun getString(key: String): String? =
        platformSynchronized(lock = this) { data[key] }

    override fun setupBlocking() {
        // Lazy invocation
        data
    }

    override suspend fun setup() {
        asyncData.await()
    }

    private fun encodeData() = dataTransformer.encode (
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
