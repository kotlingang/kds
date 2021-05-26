@file:OptIn(DelicateCoroutinesApi::class)

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.files.File
import `fun`.kotlingang.kds.files.Files
import `fun`.kotlingang.kds.manager.FileDataManager
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json


typealias KFileDataStorage = KAsyncDataStorage

fun KFileDataStorage (
    absolutePath: String,
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob() + CoroutineName("KDS Coroutine")
): KFileDataStorage = KFileDataStorage (
    json, scope,
    FileDataManager(File(absolutePath))
)

fun KFileDataStorage (
    name: String,
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob() + CoroutineName("KDS Coroutine"),
    @Suppress("UNUSED_PARAMETER")
    unused: Nothing? = null
): KFileDataStorage = KFileDataStorage (
    json, scope,
    FileDataManager(Files.homeDir.join(path = "data").join(path = "$name.json"))
)

fun KFileDataStorage (
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob() + CoroutineName("KDS Coroutine")
): KFileDataStorage = KFileDataStorage (
    json, scope, FileDataManager(Files.homeDir.join(path = "data").join(path = "data.json"))
)
