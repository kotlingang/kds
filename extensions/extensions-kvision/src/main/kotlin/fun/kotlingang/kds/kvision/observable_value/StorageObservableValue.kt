package `fun`.kotlingang.kds.kvision.observable_value

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.optional.getOrDefault
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import io.kvision.state.MutableState
import io.kvision.state.ObservableValue
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


public inline fun <reified T> KTypeDataStorage.observableValue(): StorageObservableValueProvider<T?> =
    observableValue { null }

@OptIn(UnsafeKType::class, ExperimentalStdlibApi::class)
public inline fun <reified T> KTypeDataStorage.observableValue (
    noinline default: () -> T
): StorageObservableValueProvider<T> = StorageObservableValueProvider(storage = this, typeOf<T>(), default)

@OptIn(UnsafeKType::class)
public class StorageObservableValueProvider<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val type: KType,
    private val default: () -> T
) {
    private lateinit var name: String
    private val state by lazy { StorageObservableValue(storage, name, type, default) }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): StorageObservableValue<T> {
        name = property.name
        return state
    }
}

@OptIn(UnsafeKType::class, RawSetterGetter::class)
public class StorageObservableValue<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val key: String,
    private val type: KType,
    default: () -> T
) : MutableState<T> {
    private val observable = ObservableValue (
        storage.getWithKType<T>(key, type).getOrDefault(default)
    )

    public var value: T by observable::value

    override fun setState(state: T) {
        storage.putWithKType(key, type, state)
        observable.setState(state)
    }

    override fun getState(): T = observable.getState()

    override fun subscribe(observer: (T) -> Unit): () -> Unit = observable.subscribe(observer)

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setState(value)
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getState()
}
