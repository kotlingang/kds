@file:Suppress("MemberVisibilityCanBePrivate")

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.components.AsyncCommitPerformer
import `fun`.kotlingang.kds.data_manager.AsyncContentDataManager
import `fun`.kotlingang.kds.sync.platformSynchronized
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


class KAsyncDataStorage @OptIn(DelicateCoroutinesApi::class) constructor (
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob() + CoroutineName("KDS Coroutine"),
    private val manager: AsyncContentDataManager
) : KBlockingDataStorage(json, manager) {
    private var asyncData: Map<String, JsonElement>? = null

    override fun getOrLoadData() = platformSynchronized(lock = this) {
        asyncData ?: manager.loadDataBlocking().parseData().also { asyncData = it }
    }.toMutableMap()

    private val loadDataMutex = Mutex()

    /**
     * The worst case may be when data will be loaded twice but it is not problem
     * since library is recommended to use on small data.
     * *It is impossible when using functions with their contacts.
     */
    suspend fun loadData() = loadDataMutex.withLock {
        if(asyncData != null) {
            val data = manager.loadData().parseData()
            platformSynchronized(lock = this) {
                if (asyncData != null) {
                    asyncData = data
                }
            }
        }
    }

    private val commitPerformer = AsyncCommitPerformer(scope) { manager.saveData(data.encodeData()) }

    suspend fun commit() = commitPerformer.commit()
    fun launchCommit() = commitPerformer.launchCommit()

    override fun performAutoSave() {
        if(autoSave)
            launchCommit()
    }
}
