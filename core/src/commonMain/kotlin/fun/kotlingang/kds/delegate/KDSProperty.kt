package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import `fun`.kotlingang.kds.optional.getOrDefault
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


public inline fun <reified T> KTypeDataStorage.property(): KDSProperty<T?> = property { null }

@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.property(noinline default: () -> T): KDSProperty<T> =
    KDSProperty(storage = this, typeOf<T>(), default)

@OptIn(RawSetterGetter::class)
public class KDSProperty<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val type: KType,
    private val default: () -> T
) {
    @OptIn(UnsafeKType::class)
    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T): Unit =
        storage.putWithKType(key = property.name, type, value)

    @OptIn(UnsafeKType::class)
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        storage.getWithKType<T>(key = property.name, type).getOrDefault {
            val default = default()
            storage.putWithKType(property.name, type, default)
            return@getOrDefault default
        }
}
