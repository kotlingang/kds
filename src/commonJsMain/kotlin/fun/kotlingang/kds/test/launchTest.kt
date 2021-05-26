package `fun`.kotlingang.kds.test

import `fun`.kotlingang.kds.extensions.any.unit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


actual fun CoroutineScope.launchTest(block: suspend CoroutineScope.() -> Unit) = launch { block() }.unit
