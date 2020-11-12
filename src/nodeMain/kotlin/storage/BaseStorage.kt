package com.kotlingang.kds.storage

import fs.MakeDirectoryOptions
import kotlinx.coroutines.CompletableDeferred
import path.path


private val fs = js("require('fs')")
private val pathModule = path


internal actual class BaseStorage actual constructor(actual val path: String){
    init {
        if(!fs.existsSync(path) as Boolean) {
            val parent = pathModule.resolve(path, "..")

            val options = object : MakeDirectoryOptions {
                override var recursive: Boolean? = true
            }
            fs.mkdirSync(parent, options)

            fs.appendFileSync(path, "{}")
        }
    }

    actual suspend fun saveStorage(text: String) {
        val deferred = CompletableDeferred<Unit>()
        fs.writeFile(path, text) { _ ->
            deferred.complete(Unit)
        }
        return deferred.await()
    }

    actual suspend fun loadStorage(): String? {
        val deferred = CompletableDeferred<String?>()
        fs.readFile(path, "utf8") { _, data ->
            deferred.complete(data as? String)
        }
        return deferred.await()
    }
}
