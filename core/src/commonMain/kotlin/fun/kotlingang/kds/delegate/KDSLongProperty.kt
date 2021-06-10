package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public fun PrimitiveDataStorage.long(default: () -> Long): KDSLongProperty =
    KDSLongProperty(storage = this, default)

@OptIn(RawSetterGetter::class)
public class KDSLongProperty (
    private val storage: PrimitiveDataStorage,
    private val default: () -> Long
) : ReadWriteProperty<Any?, Long> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Long =
        storage.getLong(property.name) ?: default()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        storage.putLong(property.name, value)
    }
}
