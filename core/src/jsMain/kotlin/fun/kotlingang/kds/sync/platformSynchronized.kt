package `fun`.kotlingang.kds.sync


@PublishedApi
internal actual inline fun <R> platformSynchronized(lock: Any, block: () -> R) = block()
