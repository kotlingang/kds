package `fun`.kotlingang.kds.manager

import `fun`.kotlingang.kds.files.File
import kotlinx.coroutines.*
import java.io.File as JavaFile


internal actual class FileDataManager actual constructor(file: File) : AsyncDataManager {
    private val javaFile = JavaFile(file.path)

    init {
        if(!javaFile.exists()) {
            javaFile.parentFile?.mkdirs()
            javaFile.createNewFile()
            javaFile.writeText(text = "{}")
        }
    }

    override fun loadDataBlocking() = javaFile.readText()
    override fun saveDataBlocking(text: String) = javaFile.writeText(text)

    private val bufferSize = DEFAULT_BUFFER_SIZE

    override suspend fun loadData(): String = withContext(Dispatchers.IO) {
        buildString {
            val buffer = CharArray(bufferSize)
            javaFile.bufferedReader(bufferSize = bufferSize).use { reader ->
                @Suppress("BlockingMethodInNonBlockingContext")
                while(true) {
                    val readCount = reader.read(buffer).takeIf { it >= 0 } ?: return@use
                    append(buffer.slice(0 until readCount).joinToString(separator = ""))
                    yield()
                }
            }
        }
    }

    override suspend fun saveData(text: String) = withContext(Dispatchers.IO) {
        javaFile.bufferedWriter(bufferSize = bufferSize).use { writer ->
            text.chunked(size = bufferSize).forEach { text ->
                writer.write(text)
                yield()
            }
        }
    }
}
