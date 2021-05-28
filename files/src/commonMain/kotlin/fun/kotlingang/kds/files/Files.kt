package `fun`.kotlingang.kds.files


expect object Files {
    val homeDir: CommonFileInterface
}

expect class CommonFileInterface(absolutePath: String) {
    val absolutePath: String
    fun join(path: String): CommonFileInterface
}
