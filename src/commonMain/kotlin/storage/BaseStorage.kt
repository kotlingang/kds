package com.kotlingang.kds.storage


internal expect class BaseStorage(path: String) {
    val path: String

    suspend fun saveStorage(text: String)
    suspend fun loadStorage(): String?
}
