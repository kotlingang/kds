package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.components.LocalStorageDataManager
import kotlinx.serialization.json.Json


typealias KLocalDataStorage = KJsonPropertyDataStorage
fun KLocalDataStorage(json: Json = Json): KLocalDataStorage =
    KJsonPropertyDataStorage(json, LocalStorageDataManager)
