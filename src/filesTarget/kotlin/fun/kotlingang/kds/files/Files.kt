package `fun`.kotlingang.kds.files


expect object Files {
    val homeDir: File
}

expect class File(path: String) {
    val path: String
    fun join(path: String): File
}
