package `fun`.kotlingang.kds.sync

import `fun`.kotlingang.kds.annotation.InternalKDSApi


@InternalKDSApi
public actual inline fun <R> platformSynchronized(lock: Any, crossinline block: () -> R): R = synchronized(lock, block)
