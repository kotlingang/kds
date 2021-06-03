package `fun`.kotlingang.kds.storage

import kotlinx.serialization.KSerializer


interface SerializableDataStorage {
    fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T)
    fun <T> getSerializable(key: String, serializer: KSerializer<T>, default: () -> T): T
}
