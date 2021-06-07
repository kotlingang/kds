package `fun`.kotlingang.kds.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File


public actual class CommonFile(private val file: File) {
    public actual constructor(absolutePath: String) : this(File(absolutePath))

    public actual val absolutePath: String = file.absolutePath

    public actual fun join(path: String): CommonFile = CommonFile(File(file, path))
    public actual val exists: Boolean get() = file.exists()
    public actual val parentFile: CommonFile? get() = file.parentFile?.let(::CommonFile)

    public actual fun mkdir(recursive: Boolean) {
        when(recursive) {
            true -> file.mkdirs()
            false -> file.mkdir()
        }
    }
    public actual fun createNewFile(defaultText: String) {
        file.createNewFile()
        file.writeText(defaultText)
    }

    private val bufferSize = DEFAULT_BUFFER_SIZE

    public actual fun writeTextBlocking(text: String): Unit = file.writeText(text)

    public actual suspend fun writeText(text: String): Unit = withContext(Dispatchers.IO) {
        file.bufferedWriter(bufferSize = bufferSize).use { writer ->
            text.chunked(size = bufferSize).forEach { text ->
                yield()
                writer.write(text)
            }
        }
    }

    public actual fun readTextBlocking(): String = file.readText()

    public actual suspend fun readText(): String = withContext(Dispatchers.IO) {
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

    public actual companion object {
        public actual val HOME_DIR: CommonFile = CommonFile(System.getProperty("user.dir"))
    }
}
