package `fun`.kotlingang.kds

import kotlinx.coroutines.*


actual fun CoroutineScope.runTestBlocking(block: suspend CoroutineScope.() -> Unit) {
    val promise = promise {
        block()
    }
    eval("(promise) => async () => await promise")(promise)()
}

/**
 * @return true if current target can run coroutine blocking
 */
actual fun <T> runBlockingPlatform(block: suspend CoroutineScope.() -> T): Pair<Boolean, T?> = false to null
