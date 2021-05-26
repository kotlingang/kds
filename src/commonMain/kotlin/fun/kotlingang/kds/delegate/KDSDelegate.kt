package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.KBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> KDSDelegate (
    noinline default: () -> T
) = KDSDelegate(typeOf<T>(), default)


@Suppress("UNCHECKED_CAST")
@OptIn(DelicateKDSApi::class)
class KDSDelegate<T> private constructor (
    /** @contract Union */ private val serializer: KSerializer<T>?,
    /** @contract Union */ private val type: KType?,
    private val default: () -> T
) {
    constructor(serializer: KSerializer<T>?, default: () -> T) : this(serializer, type = null, default)
    @PublishedApi
    internal constructor(type: KType, default: () -> T) : this(serializer = null, type, default)

    operator fun getValue(storage: KBlockingDataStorage, property: KProperty<*>) =
        storage.get (
            property.name,
            serializer = serializer ?: storage.json.serializersModule.serializer (
                type = type ?: error("Contract error")
            ) as KSerializer<T>,
            default
        )

    operator fun setValue(storage: KBlockingDataStorage, property: KProperty<*>, value: T) =
        storage.set (
            property.name,
            serializer = serializer ?: storage.json.serializersModule.serializer (
                type = type ?: error("Contract error")
            ) as KSerializer<T>,
            value
        )
}
