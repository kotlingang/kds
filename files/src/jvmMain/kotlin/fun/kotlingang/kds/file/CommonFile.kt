package `fun`.kotlingang.kds.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File


actual class CommonFile(private val file: File) {
    actual constructor(absolutePath: String) : this(File(absolutePath))

    actual val absolutePath: String = file.absolutePath

    actual fun join(path: String) = CommonFile(File(file, path))
    actual val exists get() = file.exists()
    actual val parentFile get() = file.parentFile?.let(::CommonFile)

    actual fun mkdir(recursive: Boolean) {
        when(recursive) {
            true -> file.mkdirs()
            false -> file.mkdir()
        }
    }
    actual fun createNewFile(defaultText: String) {
        file.createNewFile()
        file.writeText(defaultText)
    }

    private val bufferSize = DEFAULT_BUFFER_SIZE

    actual fun writeTextBlocking(text: String) = file.writeText(text)

    actual suspend fun writeText(text: String) = withContext(Dispatchers.IO) {
        file.bufferedWriter(bufferSize = bufferSize).use { writer ->
            text.chunked(size = bufferSize).forEach { text ->
                yield()
                writer.write(text)
            }
        }
    }

    actual fun readTextBlocking(): String = file.readText()

    actual suspend fun readText(): String = withContext(Dispatchers.IO) {
        buildString {
            val buffer = CharArray(bufferSize)
            file.bufferedReader(bufferSize = bufferSize).use { reader ->
                @Suppress("BlockingMethodInNonBlockingContext")
                while(true) {
                    yield()
                    val readCount = reader.read(buffer).takeIf { it >= 0 } ?: return@use
                    append(buffer.slice(0 until readCount).joinToString(separator = ""))
                }
            }
        }
    }

    actual companion object {
        actual val HOME_DIR = CommonFile(System.getProperty("user.dir"))
    }
}
