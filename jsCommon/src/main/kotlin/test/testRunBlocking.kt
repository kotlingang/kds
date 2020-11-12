package com.kotlingang.kds.test

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext


val testScope = MainScope()
val testCoroutineContext: CoroutineContext = testScope.coroutineContext

fun runTestBlocking(block: suspend () -> Unit) {
    val func = eval("""(promise) => async () => { await promise }""")
    val promise = testScope.promise { block() }
    func(promise)()
}
