package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


actual fun <T> CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> T)
        = runBlocking(coroutineContext, block)

