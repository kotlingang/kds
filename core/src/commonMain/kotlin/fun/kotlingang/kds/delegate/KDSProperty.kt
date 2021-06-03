package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.KJsonBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.property(noinline default: () -> T) =
    KDSProperty(storage = this, typeOf<T>(), default)

@OptIn(DelicateKDSApi::class)
class KDSProperty<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val type: KType,
    private val default: () -> T
) {
    @OptIn(UnsafeKType::class)
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        storage.putWithKType(key = property.name, type, value)

    @OptIn(UnsafeKType::class)
    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        storage.getWithKType(key = property.name, type, default)
}
