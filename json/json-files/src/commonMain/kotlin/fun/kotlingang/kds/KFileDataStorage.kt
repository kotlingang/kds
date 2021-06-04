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
class KFileDataStorage private constructor (
    json: Json,
    private val storage: KFileStringDataStorage
) : KJsonDataStorage(json, storage), AsyncCommittableStorage {

    internal constructor (
        file: CommonFile,
        json: Json,
        scope: CoroutineScope
    ) : this(json, KFileStringDataStorage(file, scope, JsonDataTransformer(json)))

    constructor (
        absolutePath: String,
        json: Json = Json,
        scope: CoroutineScope = GlobalScope + SupervisorJob()
    ) : this(CommonFile(absolutePath), json, scope)

    constructor (
        json: Json = Json,
        scope: CoroutineScope = GlobalScope + SupervisorJob()
    ) : this (
        CommonFile.HOME_DIR.join(path = "data").join(path = "data.json"),
        json, scope
    )

    companion object {
        fun ofName (
            name: String,
            json: Json = Json,
            scope: CoroutineScope = GlobalScope + SupervisorJob()
        ) = KFileDataStorage (
            CommonFile.HOME_DIR.join(path = "data").join(path = "$name.json"),
            json, scope
        )
    }

    override suspend fun setup() = storage.setup()
    override fun setupBlocking() = storage.setupBlocking()

    @OptIn(RawSetterGetter::class, DelicateKDSApi::class)
    private fun applyMutations() = encodeReferences().forEach { (k, v) ->
        storage.putString(k, v)
    }

    override fun launchCommit() {
        platformSynchronized(lock = this) {
            applyMutations()
        }
        storage.launchCommit()
    }

    override suspend fun commit() {
        platformSynchronized(lock = this) {
            applyMutations()
        }
        storage.commit()
    }

    override fun commitBlocking() {
        platformSynchronized(lock = this) {
            applyMutations()
        }
        storage.commitBlocking()
    }
}
