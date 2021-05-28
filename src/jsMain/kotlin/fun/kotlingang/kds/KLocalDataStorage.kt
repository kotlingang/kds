package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.manager.BlockingDataManager
import `fun`.kotlingang.kds.manager.LocalStorageDataManager
import kotlinx.serialization.json.Json


typealias KLocalDataStorage = KBlockingDataStorage
fun KLocalDataStorage(key: String = "data", json: Json = Json): KLocalDataStorage =
    KBlockingDataStorage(json, LocalStorageDataManager(key))
