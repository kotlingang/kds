package com.kotlingang.kds.utils

import com.kotlingang.kds.test.runTestBlocking


actual val testCoroutineContext = com.kotlingang.kds.test.testCoroutineContext
actual fun runTestBlocking(block: suspend () -> Unit) = runTestBlocking(block)
