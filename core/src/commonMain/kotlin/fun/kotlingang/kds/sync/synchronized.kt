package `fun`.kotlingang.kds.sync

import `fun`.kotlingang.kds.annotation.InternalKDSApi


@InternalKDSApi
expect inline fun <R> platformSynchronized(lock: Any, crossinline block: () -> R): R
