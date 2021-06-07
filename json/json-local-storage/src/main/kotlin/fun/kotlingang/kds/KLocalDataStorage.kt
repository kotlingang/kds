package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.CommittableStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


public open class KLocalDataStorage (
    json: Json = Json
) : KJsonDataStorage(json, JsonElementLocalDataStorage(json)), CommittableStorage {
    @OptIn(DelicateKDSApi::class, RawSetterGetter::class)
    final override fun commitBlocking(): Unit = encodeReferences()
        .forEach { (k, v) -> KStringLocalDataStorage.putString(k, json.encodeToString(v)) }

    final override fun setupBlocking() {
        super.setupBlocking()
    }
}
