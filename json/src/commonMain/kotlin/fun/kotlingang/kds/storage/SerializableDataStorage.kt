package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.optional.Optional
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


interface SerializableDataStorage : KTypeDataStorage {
    val json: Json

    @RawSetterGetter
    fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T)

    @RawSetterGetter
    fun <T> getSerializable(key: String, serializer: KSerializer<T>): Optional<T>
}
