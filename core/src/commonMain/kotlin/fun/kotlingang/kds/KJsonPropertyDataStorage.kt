package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.manager.PropertyDataManager
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import `fun`.kotlingang.kds.storage.SerializableDataStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType


open class KJsonPropertyDataStorage (
    val json: Json,
    private val manager: PropertyDataManager
) : SerializableDataStorage, KTypeDataStorage {
    @UnsafeKType
    override fun putWithKType(key: String, type: KType, value: Any?) =
        putSerializable(key, json.serializersModule.serializer(type), value)

    @Suppress("UNCHECKED_CAST")
    @UnsafeKType
    override fun <T> getWithKType(key: String, type: KType, default: () -> T): T =
        getSerializable(key, json.serializersModule.serializer(type) as KSerializer<T>, default)

    override fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T) =
        manager.put(key, json.encodeToString(serializer, value))

    override fun <T> getSerializable(key: String, serializer: KSerializer<T>, default: () -> T) =
        manager.get(key)?.let { json.decodeFromString(serializer, it) } ?: default()
}
