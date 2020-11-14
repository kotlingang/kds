package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise


actual fun CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> Unit) {
    val promise = promise {
        block()
    }
    eval("(promise) => (async () => await promise)()")(promise)
}
