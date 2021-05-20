package `fun`.kotlingang.kds.storage

import process


private val pathModule = js("require('path')")

actual val dirPath = process.cwd()

actual fun String.joinPath(path: String) = pathModule.join(this, path) as String
