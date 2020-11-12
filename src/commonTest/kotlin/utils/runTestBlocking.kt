package com.kotlingang.kds.utils

import kotlin.coroutines.CoroutineContext


expect val testCoroutineContext: CoroutineContext
expect fun runTestBlocking(block: suspend () -> Unit)