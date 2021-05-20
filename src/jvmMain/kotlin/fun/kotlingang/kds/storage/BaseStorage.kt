package `fun`.kotlingang.kds.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.File
import java.io.IOException


@Suppress("BlockingMethodInNonBlockingContext", "CanBeParameter")
internal actual class BaseStorage actual constructor(actual val path: String) {

    private val file = File(path).apply {
        try { parentFile.mkdirs() } catch (t: Throwable) {}
        createNewFile()
    }

    actual suspend fun saveStorage(text: String) = withContext(Dispatchers.IO) {
        file.writeText(text)
    }

    actual suspend fun loadStorage(): String? = withContext(Dispatchers.IO) {
        file.readText().takeIf(String::isNotBlank)
    }
}
