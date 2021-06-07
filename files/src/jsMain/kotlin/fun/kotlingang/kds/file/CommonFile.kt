package `fun`.kotlingang.kds.file

import fs.MakeDirectoryOptions
import kotlinx.coroutines.await
import process
import path.path as pathModule


public actual class CommonFile actual constructor (
    public actual val absolutePath: String
) {
    public actual fun join(path: String): CommonFile = CommonFile(pathModule.join(path))

    public actual val exists: Boolean get() = fs.existsSync(absolutePath)
    // Issue: #19
    @Suppress("RedundantNullableReturnType")
    public actual val parentFile: CommonFile? get() = CommonFile(pathModule.resolve(absolutePath, ".."))

    public actual fun mkdir(recursive: Boolean) {
        fs.mkdirSync(absolutePath, object : MakeDirectoryOptions {
            override var recursive: Boolean? = recursive
        })
    }

    public actual fun createNewFile(defaultText: String) {
        writeTextBlocking(defaultText)
    }

    public actual fun writeTextBlocking(text: String) {
        fs.writeFileSync(absolutePath, text, options = null as String?)
    }

    public actual suspend fun writeText(text: String) {
        fs.promises.writeFile(absolutePath, text, options = null as String?).await()
    }

    public actual fun readTextBlocking(): String =
        fs.readFileSync(absolutePath, options = "utf8")

    public actual suspend fun readText(): String =
        fs.promises.readFile(absolutePath, options = "utf8").await() as String

    public actual companion object {
        public actual val HOME_DIR: CommonFile = CommonFile(process.cwd())
    }
}
