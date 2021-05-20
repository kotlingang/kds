package `fun`.kotlingang.kds.storage

import java.nio.file.Paths


actual val dirPath: String = System.getProperty("user.dir")

actual fun String.joinPath(path: String) = Paths.get(this, path).toString()
