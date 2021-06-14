package `fun`.kotlingang.kds.coroutines.mutable_state_flow

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.optional.getOrDefault
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


public inline fun <reified T> KTypeDataStorage.mutableStateFlow(): StorageMutableStateFlowProvider<T?> =
    mutableStateFlow { null }

@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.mutableStateFlow (
    noinline defaultValue: () -> T,
): StorageMutableStateFlowProvider<T> = StorageMutableStateFlowProvider (
    storage = this,
    type = typeOf<T>(),
    defaultValue = defaultValue
)

@OptIn(UnsafeKType::class)
public class StorageMutableStateFlowProvider<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val type: KType,
    private val defaultValue: () -> T
) {
    private lateinit var name: String
    private val state by lazy { StorageMutableStateFlow(storage, name, type, defaultValue) }

    public operator fun getValue (
        thisRef: Any?, property: KProperty<*>
    ): StorageMutableStateFlow<T> {
        name = property.name
        return state
    }
}

@OptIn(RawSetterGetter::class, UnsafeKType::class)
public class StorageMutableStateFlow<T> @UnsafeKType constructor (
    private val storage: KTypeDataStorage,
    private val key: String,
    private val type: KType,
    defaultValue: () -> T
) : MutableStateFlow<T> {
    private val state = MutableStateFlow (
        storage.getWithKType<T>(key, type).getOrDefault(defaultValue)
    )

    override var value: T
        get() = state.value
        set(value) {
            storage.putWithKType(key, type, value)
            state.value = value
        }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        state.collect(collector)
    }

    override val subscriptionCount: StateFlow<Int> = state.subscriptionCount

    override suspend fun emit(value: T) {
        state.emit(value)
        storage.putWithKType(key, type, value)
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        state.resetReplayCache()
    }

    override fun tryEmit(value: T): Boolean = state.tryEmit(value).also { emitted ->
        if (emitted) storage.putWithKType(key, type, value)
    }

    override fun compareAndSet(expect: T, update: T): Boolean =
        state.compareAndSet(expect, update).also { set ->
            if (set) storage.putWithKType(key, type, update)
        }

    override val replayCache: List<T> = state.replayCache
}
