package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking


actual fun CoroutineScope.runTestBlocking(block: suspend CoroutineScope.() -> Unit)
        = runBlocking(coroutineContext, block)

/**
 * @return true if current target can run coroutine blocking
 */
actual fun CoroutineScope.runBlockingPlatform(block: suspend CoroutineScope.() -> Unit)
        = runTestBlocking(block).let { true }
