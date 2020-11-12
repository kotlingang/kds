package com.kotlingang.kds.storage

import kotlinx.browser.localStorage


internal actual class BaseStorage actual constructor(actual val path: String) {

    actual suspend fun saveStorage(text: String) = localStorage.setItem(path, text)
    actual suspend fun loadStorage() = localStorage.getItem(path)

}
