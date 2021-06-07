package `fun`.kotlingang.kds.file


public expect class CommonFile(absolutePath: String) {
    public val absolutePath: String

    public val exists: Boolean
    public val parentFile: CommonFile?

    public fun join(path: String): CommonFile

    public fun mkdir(recursive: Boolean = false)
    public fun createNewFile(defaultText: String = "")

    public fun writeTextBlocking(text: String)
    public suspend fun writeText(text: String)

    public fun readTextBlocking(): String
    public suspend fun readText(): String

    public companion object {
        public val HOME_DIR: CommonFile
    }
}
