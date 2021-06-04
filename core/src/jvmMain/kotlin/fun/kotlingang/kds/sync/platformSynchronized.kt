package `fun`.kotlingang.kds.sync

import `fun`.kotlingang.kds.annotation.InternalKDSApi


@InternalKDSApi
actual inline fun <R> platformSynchronized(lock: Any, crossinline block: () -> R) = synchronized(lock, block)
