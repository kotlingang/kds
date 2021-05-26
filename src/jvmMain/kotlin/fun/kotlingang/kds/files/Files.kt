package `fun`.kotlingang.kds.files

import java.io.File as JavaFile


actual object Files {
    actual val homeDir = File(System.getProperty("user.dir"))
}


actual class File actual constructor(actual val path: String) {
    private val selfPath = path
    actual fun join(path: String) = File(JavaFile(selfPath, path).absolutePath)
}
