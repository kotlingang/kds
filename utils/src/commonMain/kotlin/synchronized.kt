package com.kotlingang.kds


expect inline fun <R> synchronized(lock: Any, block: () -> R): R
