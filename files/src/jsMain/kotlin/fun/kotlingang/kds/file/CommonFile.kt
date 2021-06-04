package `fun`.kotlingang.kds.file

import fs.MakeDirectoryOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.await
import process
import path.path as pathModule


actual class CommonFile actual constructor(actual val absolutePath: String) {
    actual fun join(path: String) = CommonFile(pathModule.join(path))

    actual val exists get() = fs.existsSync(absolutePath)
    @Suppress("RedundantNullableReturnType")
    actual val parentFile: CommonFile? get() = CommonFile(pathModule.resolve(absolutePath, ".."))

    actual fun mkdir(recursive: Boolean) =
        fs.mkdirSync(absolutePath, object : MakeDirectoryOptions {
            override var recursive: Boolean? = recursive
        })

    actual fun createNewFile(defaultText: String) {
        writeTextBlocking(defaultText)
    }

    actual fun writeTextBlocking(text: String) =
        fs.writeFileSync(absolutePath, text, options = null as String?)

    actual suspend fun writeText(text: String) =
        fs.promises.writeFile(absolutePath, text, options = null as String?).await()

    actual fun readTextBlocking() =
        fs.readFileSync(absolutePath, options = "utf8")

    actual suspend fun readText() =
        fs.promises.readFile(absolutePath, options = "utf8").await()

    actual companion object {
        actual val HOME_DIR = CommonFile(process.cwd())
    }
}
