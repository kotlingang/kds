package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex


/**
 * Spin locker based on coroutine mutex.
 * Be careful and call it only from blocking functions.
 */
class SpinLocker : CoroutineScope {
    private val mutex = Mutex()

    fun lock() {
        val job = launch {
            mutex.lock()
        }
        while(!job.isCompleted)
            continue
    }
    fun unlock() {
        val job = launch {
            mutex.unlock()
        }
        while(!job.isCompleted)
            continue
    }

    override val coroutineContext = GlobalScope.coroutineContext + Job()
}

/**
 * To prevent infinity looping use only blocking methods inside [block]
 */
inline fun <R> SpinLocker.withLock(block: () -> R): R {
    lock()
    try {
        return block()
    } finally {
        unlock()
    }
}
