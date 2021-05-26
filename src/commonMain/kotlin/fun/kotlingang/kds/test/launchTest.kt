package `fun`.kotlingang.kds.test

import kotlinx.coroutines.CoroutineScope


/**
 * Has only guarantee that [block] will be invoked, but no guarantee when
 */
expect fun CoroutineScope.launchTest(block: suspend CoroutineScope.() -> Unit)
