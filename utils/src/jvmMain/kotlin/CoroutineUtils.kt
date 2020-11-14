package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


actual fun CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(coroutineContext, block)
}
