@file:Suppress("UNCHECKED_CAST", "FunctionName")

package `fun`.kotlingang.kds.value

import `fun`.kotlingang.kds.KDataStorage
import `fun`.kotlingang.kds.StorageConfigBuilder
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> KValueStorage (
    default: T,
    coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope + SupervisorJob(),
    noinline builder: StorageConfigBuilder = {}
) = KStoredValue(default, typeOf<T>(), coroutineScope, builder)


class KStoredValue<T> internal constructor (
    default: T,
    serializer: KSerializer<T>?,
    type: KType?,
    coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope + SupervisorJob(),
    builder: StorageConfigBuilder = {}
) : KDataStorage(name = "value", coroutineScope, builder) {

    constructor (
        default: T,
        serializer: KSerializer<T>,
        coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope + SupervisorJob(),
        builder: StorageConfigBuilder = {}
    ) : this(default, serializer, type = null, coroutineScope, builder)

    constructor (
        default: T,
        type: KType,
        coroutineScope: CoroutineScope = @OptIn(DelicateCoroutinesApi::class) GlobalScope + SupervisorJob(),
        builder: StorageConfigBuilder = {}
    ) : this(default, serializer = null, type, coroutineScope, builder)

    private val serializer = serializer ?: json.serializersModule.serializer(type!!) as KSerializer<T>

    private var value by property(this.serializer, default)

    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = ::value
}
