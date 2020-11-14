package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise


actual fun <T> CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> T): T {
    val promise = promise {
        block()
    }
    return eval("(promise) => (async () => await promise)()")(promise) as T
}
