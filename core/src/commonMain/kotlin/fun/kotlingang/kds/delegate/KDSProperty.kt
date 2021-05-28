package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.KBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty


@Suppress("unused")
fun <T> KBlockingDataStorage.property(serializer: KSerializer<T>, default: () -> T) =
    KDSProperty(storage = this, serializer, default)

inline fun <reified T> KBlockingDataStorage.property(noinline default: () -> T) =
    KDSProperty(storage = this, json.serializersModule.serializer(), default)

@OptIn(DelicateKDSApi::class)
class KDSProperty<T> (
    private val storage: KBlockingDataStorage,
    private val serializer: KSerializer<T>,
    private val default: () -> T
) {
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        storage.set(name = property.name, serializer, value)

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        storage.get(name = property.name, serializer, default)
}
