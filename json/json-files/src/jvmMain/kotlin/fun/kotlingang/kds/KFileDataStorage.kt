package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.file.CommonFile
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.io.File


@OptIn(DelicateCoroutinesApi::class)
fun KFileDataStorage (
    file: File,
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob()
): KFileDataStorage = KFileDataStorage(CommonFile(file), json, scope)
