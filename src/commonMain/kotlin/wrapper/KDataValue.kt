@file:Suppress("UNCHECKED_CAST", "FunctionName")

package com.kotlingang.kds.wrapper

import com.kotlingang.kds.KDataStorage
import com.kotlingang.kds.StorageConfigBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf


@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> KValueStorage (
        default: T, noinline builder: StorageConfigBuilder = {}
) = KStoredValue(default, typeOf<T>(), builder)


class KStoredValue<T> internal constructor (
        default: T, serializer: KSerializer<T>?, type: KType?, builder: StorageConfigBuilder
) : KDataStorage("value", builder) {

    constructor(default: T, serializer: KSerializer<T>, builder: StorageConfigBuilder = {})
            : this(default, serializer, null, builder)
    constructor(default: T, type: KType, builder: StorageConfigBuilder = {})
            : this(default, null, type, builder)


    private val serializer = serializer ?: json.serializersModule.serializer(type!!) as KSerializer<T>

    private var value by property(this.serializer, default)

    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = this::value
}
