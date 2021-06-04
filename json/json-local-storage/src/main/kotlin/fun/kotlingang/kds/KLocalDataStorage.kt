package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.CommittableStorage
import kotlinx.serialization.json.Json


class KLocalDataStorage (
    json: Json = Json
) : KJsonDataStorage(json, KStringLocalDataStorage), CommittableStorage {

    @OptIn(DelicateKDSApi::class, RawSetterGetter::class)
    override fun commitBlocking() = encodeReferences()
        .forEach { (k, v) -> KStringLocalDataStorage.putString(k, v) }
    
}
