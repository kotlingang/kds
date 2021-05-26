package `fun`.kotlingang.kds.composition

import `fun`.kotlingang.kds.extensions.any.unit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * Used to remove redundant async savings
 */
class AsyncCommitPerformer internal constructor (
    private val scope: CoroutineScope,
    private val asyncCommittable: AsyncCommittable
){
    private val mutex = Mutex()
    private var job: Job? = null

    suspend fun commit() = mutex.withLock {
        job?.cancelAndJoin()
        job = scope.launch {
            asyncCommittable.asyncCommit()
        }
        job?.join() ?: error("Contract error")
    }

    fun launchCommit() = scope.launch { commit() }.unit
}

internal fun interface AsyncCommittable {
    suspend fun asyncCommit()
}
