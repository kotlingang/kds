package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.value.Optional
import kotlin.reflect.KType
import kotlin.reflect.typeOf


interface KTypeDataStorage {
    /**
     * May be you would use inline [putWithKType]?
     */
    @RawSetterGetter
    @UnsafeKType
    fun putWithKType(key: String, type: KType, value: Any?)

    /**
     * May be you would use inline [getWithKType]?
     */
    @RawSetterGetter
    @UnsafeKType
    fun <T> getWithKType(key: String, type: KType): Optional<T>
}

@RawSetterGetter
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.putWithKType(key: String, value: T) =
    putWithKType(key, typeOf<T>(), value)

@RawSetterGetter
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.getWithKType(key: String): Optional<T> =
    getWithKType(key, typeOf<T>())
