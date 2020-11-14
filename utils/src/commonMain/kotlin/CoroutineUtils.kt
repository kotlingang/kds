package com.kotlingang.kds

import kotlinx.coroutines.CoroutineScope


expect fun CoroutineScope.runBlocking(block: suspend CoroutineScope.() -> Unit)
