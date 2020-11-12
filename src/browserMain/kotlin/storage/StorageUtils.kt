package com.kotlingang.kds.storage

// Nothing because Browser js using localStorage for saving data
actual val dirPath = ""

actual fun String.joinPath(path: String) = "$this/$path"
