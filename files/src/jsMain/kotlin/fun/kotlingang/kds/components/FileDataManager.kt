package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.manager.AsyncContentDataManager
import `fun`.kotlingang.kds.files.CommonFileInterface
import fs.MakeDirectoryOptions
import kotlinx.coroutines.CompletableDeferred
import path.path


private val fs = js("require('fs')")
private val pathModule = path

internal actual class FileDataManager actual constructor (
    file: CommonFileInterface
) : AsyncContentDataManager {

    init {
        if(!fs.existsSync(path) as Boolean) {
            val parent = pathModule.resolve(file.absolutePath, "..")
            val options = object : MakeDirectoryOptions {
                override var recursive: Boolean? = true
            }
            fs.mkdirSync(parent, options)
            fs.appendFileSync(path, "{}")
        }
    }

    override fun loadDataBlocking() = fs.readFileSync(path, "utf8") as String
    override fun saveDataBlocking(text: String) = fs.writeFileSync(path, text)

    override suspend fun loadData(): String {
        val deferred = CompletableDeferred<String>()
        fs.readFile(path, "utf8") { _, data ->
            deferred.complete(data as String)
        }
        return deferred.await()
    }
    override suspend fun saveData(text: String) {
        val deferred = CompletableDeferred<Unit>()
        fs.writeFile(path, text) { _ ->
            deferred.complete(Unit)
        }
        return deferred.await()
    }
}
