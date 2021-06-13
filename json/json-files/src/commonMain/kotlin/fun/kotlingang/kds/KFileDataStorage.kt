package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.InternalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.file.CommonFile
import `fun`.kotlingang.kds.storage.AsyncCommittableStorage
import `fun`.kotlingang.kds.sync.platformSynchronized
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json


@OptIn(DelicateCoroutinesApi::class, InternalKDSApi::class)
public expect open class KFileDataStorage : KJsonDataStorage, AsyncCommittableStorage {
    public constructor (
        absolutePath: String,
        json: Json = Json,
        scope: CoroutineScope = GlobalScope + SupervisorJob()
    )

    public constructor (
        json: Json = Json,
        scope: CoroutineScope = GlobalScope + SupervisorJob()
    )

    public companion object {
        public fun ofName (
            name: String,
            json: Json = Json,
            scope: CoroutineScope = GlobalScope + SupervisorJob()
        ): KFileDataStorage
    }
}
