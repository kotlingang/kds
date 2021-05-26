package `fun`.kotlingang.kds.files

import process
import path.path as pathModule


actual object Files {
    actual val homeDir: File = File(process.cwd())
}

actual class File actual constructor(actual val path: String) {
    actual fun join(path: String) = File(pathModule.join(path))
}
