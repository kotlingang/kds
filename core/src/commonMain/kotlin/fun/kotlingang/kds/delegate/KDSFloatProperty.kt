package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public fun PrimitiveDataStorage.float(default: () -> Float): KDSFloatProperty =
    KDSFloatProperty(storage = this, default)

@OptIn(RawSetterGetter::class)
public class KDSFloatProperty (
    private val storage: PrimitiveDataStorage,
    private val default: () -> Float
) : ReadWriteProperty<Any?, Float> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Float =
        storage.getFloat(property.name) ?: default()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        storage.putFloat(property.name, value)
    }
}
