package com.kotlingang.kds.utils

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


actual val testCoroutineContext: CoroutineContext =
    Executors.newSingleThreadExecutor().asCoroutineDispatcher()
actual fun runTestBlocking(block: suspend () -> Unit): Unit =
    runBlocking(testCoroutineContext) { block() }
