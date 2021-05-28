package `fun`.kotlingang.kds.files

import process
import path.path as pathModule


actual object Files {
    actual val homeDir: CommonFileInterface = CommonFileInterface(process.cwd())
}

actual class CommonFileInterface actual constructor(actual val absolutePath: String) {
    actual fun join(path: String) = CommonFileInterface(pathModule.join(path))
}
