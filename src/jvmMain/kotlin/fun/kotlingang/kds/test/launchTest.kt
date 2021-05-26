package `fun`.kotlingang.kds.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


actual fun CoroutineScope.launchTest(block: suspend CoroutineScope.() -> Unit) = runBlocking {
    launch { block() }.join()
}
