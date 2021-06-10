package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public fun PrimitiveDataStorage.double(default: () -> Double): KDSDoubleProperty =
    KDSDoubleProperty(storage = this, default)

@OptIn(RawSetterGetter::class)
public class KDSDoubleProperty (
private val storage: PrimitiveDataStorage,
private val default: () -> Double
) : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double =
    storage.getDouble(property.name) ?: default()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        storage.putDouble(property.name, value)
    }
}
