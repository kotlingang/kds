package `fun`.kotlingang.kds.compose.mutable_state

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.optional.getOrDefault
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.structuralEqualityPolicy
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


public inline fun <reified T> KTypeDataStorage.mutableState (
    policy: SnapshotMutationPolicy<T?> = structuralEqualityPolicy(),
): StorageMutableStateProvider<T?> = mutableState(policy) { null }


@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.mutableState (
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
    noinline defaultValue: () -> T,
): StorageMutableStateProvider<T> = StorageMutableStateProvider (
    storage = this,
    type = typeOf<T>(),
    defaultValue = defaultValue,
    policy = policy
)


@OptIn(UnsafeKType::class)
public class StorageMutableStateProvider<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val type: KType,
    private val defaultValue: () -> T,
    private val policy: SnapshotMutationPolicy<T>
) {
    private lateinit var name: String
    private val state by lazy { StorageMutableState(storage, name, type, defaultValue, policy) }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): StorageMutableState<T> {
        name = property.name
        return state
    }

}

@OptIn(RawSetterGetter::class, UnsafeKType::class)
public class StorageMutableState<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val key: String,
    private val type: KType,
    defaultValue: () -> T,
    policy: SnapshotMutationPolicy<T>
) : MutableState<T> {
    private val state = mutableStateOf (
        storage.getWithKType<T>(key, type).getOrDefault(defaultValue),
        policy = policy
    )

    override var value: T
        get() = state.value
        set(value) {
            storage.putWithKType(key, type, value)
            state.value = value
        }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { value = it }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}
