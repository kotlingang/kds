package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public fun PrimitiveDataStorage.string(default: () -> String): KDSStringProperty =
    KDSStringProperty(storage = this, default)

@OptIn(RawSetterGetter::class)
public class KDSStringProperty (
    private val storage: PrimitiveDataStorage,
    private val default: () -> String
) : ReadWriteProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String =
        storage.getString(property.name) ?: default()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        storage.putString(property.name, value)
    }
}
