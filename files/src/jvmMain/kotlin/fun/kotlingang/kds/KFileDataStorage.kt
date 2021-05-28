@file:OptIn(DelicateCoroutinesApi::class)
@file:JvmName("KFileDataStorageJvmKt")

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.manager.FileDataManager
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.io.File


fun KFileDataStorage (
    file: File,
    json: Json = Json,
    scope: CoroutineScope = GlobalScope + SupervisorJob() + CoroutineName("KDS Coroutine")
): KFileDataStorage = KFileDataStorage (
    json, scope,
    FileDataManager(file)
)
