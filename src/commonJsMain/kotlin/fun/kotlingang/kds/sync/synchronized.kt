package `fun`.kotlingang.kds.sync


internal actual inline fun <R> platformSynchronized(lock: Any, block: () -> R) = block()
