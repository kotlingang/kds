package com.kotlingang.kds.storage

import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import java.io.File
import java.io.IOException


@Suppress("BlockingMethodInNonBlockingContext", "CanBeParameter")
internal actual class BaseStorage actual constructor(actual val path: String) {

    private val file = File(path).apply {
        try { parentFile.mkdirs() } catch (t: Throwable) {}
        createNewFile()
    }

    actual suspend fun saveStorage(text: String) {
        val writer = file.writer()
        flowOf(*text.toList().toTypedArray()).collect(writer::append)
        writer.close()
    }

    actual suspend fun loadStorage(): String? {
        val reader = file.reader()

        val chars = flow {
            while(true) {
                emit(reader.read())
            }
        }.takeWhile { it != -1 }.map(Int::toChar).toList()

        return String(chars.toCharArray()).takeIf(String::isNotBlank)
    }

}
