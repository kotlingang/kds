package com.kotlingang.kds

import kotlinx.coroutines.*
import kotlin.js.Promise


actual fun CoroutineScope.runTestBlocking(block: suspend CoroutineScope.() -> Unit) {
    val promise = promise {
        block()
    }
    eval("(promise) => async () => await promise")(promise)()
}

/**
 * @return true if current target can run coroutine blocking
 */
actual fun CoroutineScope.runBlockingPlatform(block: suspend CoroutineScope.() -> Unit) = false