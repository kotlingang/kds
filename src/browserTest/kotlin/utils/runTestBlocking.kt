package com.kotlingang.kds.utils


actual val testCoroutineContext = com.kotlingang.kds.test.testCoroutineContext
actual fun runTestBlocking(block: suspend () -> Unit) = com.kotlingang.kds.test.runTestBlocking(block)
