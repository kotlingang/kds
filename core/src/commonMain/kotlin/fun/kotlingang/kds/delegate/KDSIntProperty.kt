package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public fun PrimitiveDataStorage.int(default: () -> Int): KDSIntProperty =
    KDSIntProperty(storage = this, default)

@OptIn(RawSetterGetter::class)
public class KDSIntProperty (
    private val storage: PrimitiveDataStorage,
    private val default: () -> Int
) : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int =
        storage.getInt(property.name) ?: default()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        storage.putInt(property.name, value)
    }
}
