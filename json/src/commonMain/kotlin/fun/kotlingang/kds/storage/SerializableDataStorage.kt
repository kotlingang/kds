package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.optional.Optional
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


public interface SerializableDataStorage : KTypeDataStorage {
    public val json: Json

    @RawSetterGetter
    public fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T)

    @RawSetterGetter
    public fun <T> getSerializable(key: String, serializer: KSerializer<T>): Optional<T>
}
