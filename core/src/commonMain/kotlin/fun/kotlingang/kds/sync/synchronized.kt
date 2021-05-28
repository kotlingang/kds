package `fun`.kotlingang.kds.sync


@PublishedApi
internal expect inline fun <R> platformSynchronized(lock: Any, block: () -> R): R
