package com.kotlingang.kds

import kotlinx.coroutines.sync.Mutex


/**
 * Spin locker based on coroutine mutex.
 * Be careful and call it only from blocking functions.
 */
class PlatformLocker {
    private val mutex = Mutex()

    fun lock() = runBlockingPlatform {
        mutex.lock()
    }.unit

    fun unlock() = runBlockingPlatform {
        mutex.unlock()
    }.unit
}

/**
 * To prevent infinity looping use only blocking methods inside [block]
 */
inline fun <R> PlatformLocker.withLock(block: () -> R): R {
    lock()

    try {
        return block()
    } finally {
        unlock()
    }
}
