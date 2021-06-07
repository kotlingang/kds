package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import `fun`.kotlingang.kds.optional.getOrDefault
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


inline fun <reified T> KTypeDataStorage.property() = property<T?> { null }

@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.property(noinline default: () -> T) =
    KDSProperty(storage = this, typeOf<T>(), default)

@OptIn(RawSetterGetter::class)
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
        storage.getWithKType<T>(key = property.name, type).getOrDefault {
            val default = default()
            storage.putWithKType(property.name, type, default)
            return@getOrDefault default
        }
}
