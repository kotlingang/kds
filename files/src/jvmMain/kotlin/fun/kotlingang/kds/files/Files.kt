package `fun`.kotlingang.kds.files

import java.io.File as JavaFile


actual object Files {
    actual val homeDir = CommonFileInterface(System.getProperty("user.dir"))
}


actual class CommonFileInterface actual constructor(actual val absolutePath: String) {
    private val selfPath = absolutePath
    actual fun join(path: String) = CommonFileInterface(JavaFile(selfPath, path).absolutePath)
}
