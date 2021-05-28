package `fun`.kotlingang.kds.manager

import `fun`.kotlingang.kds.files.CommonFileInterface
import kotlinx.coroutines.*
import java.io.File


internal actual class FileDataManager (
    private val file: File
) : AsyncDataManager {
    actual constructor(file: CommonFileInterface) : this(File(file.absolutePath))

    init {
        if(!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
            file.writeText(text = "{}")
        }
    }

    override fun loadDataBlocking() = file.readText()
    override fun saveDataBlocking(text: String) = file.writeText(text)

    private val bufferSize = DEFAULT_BUFFER_SIZE

    override suspend fun loadData(): String = withContext(Dispatchers.IO) {
        buildString {
            val buffer = CharArray(bufferSize)
            file.bufferedReader(bufferSize = bufferSize).use { reader ->
                @Suppress("BlockingMethodInNonBlockingContext")
                while(true) {
                    val readCount = reader.read(buffer).takeIf { it >= 0 } ?: return@use
                    append(buffer.slice(0 until readCount).joinToString(separator = ""))
                    yield()
                }
            }
        }
    }

    /**
     * The file may not be fully written, so use cancellation only if you are sure that will be OK
     */
    override suspend fun saveData(text: String) = withContext(Dispatchers.IO) {
        file.bufferedWriter(bufferSize = bufferSize).use { writer ->
            text.chunked(size = bufferSize).forEach { text ->
                yield()
                writer.write(text)
            }
        }
    }
}
