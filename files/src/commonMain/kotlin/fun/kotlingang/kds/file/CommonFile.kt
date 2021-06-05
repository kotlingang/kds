package `fun`.kotlingang.kds.file


expect class CommonFile(absolutePath: String) {
    val absolutePath: String

    val exists: Boolean
    val parentFile: CommonFile?

    fun join(path: String): CommonFile

    fun mkdir(recursive: Boolean = false)
    fun createNewFile(defaultText: String = "")

    fun writeTextBlocking(text: String)
    suspend fun writeText(text: String)

    fun readTextBlocking(): String
    suspend fun readText(): String

    companion object {
        val HOME_DIR: CommonFile
    }
}
