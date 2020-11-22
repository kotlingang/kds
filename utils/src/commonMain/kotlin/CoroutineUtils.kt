package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope


/**
 * @return true if current target can run coroutine blocking
 */
expect fun <T> runBlockingPlatform(block: suspend CoroutineScope.() -> T): Pair<Boolean, T?>

expect fun CoroutineScope.runTestBlocking(block: suspend CoroutineScope.() -> Unit)

