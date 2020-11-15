package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope


/**
 * @return true if current target can run coroutine blocking
 */
expect fun CoroutineScope.runBlockingPlatform(block: suspend CoroutineScope.() -> Unit): Boolean

expect fun CoroutineScope.runTestBlocking(block: suspend CoroutineScope.() -> Unit)

