package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope


expect fun <T> CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> T): T

fun  <T> Deferred<T>.awaitBlocking(scope: CoroutineScope = GlobalScope) = scope.runBlocking { await() }
