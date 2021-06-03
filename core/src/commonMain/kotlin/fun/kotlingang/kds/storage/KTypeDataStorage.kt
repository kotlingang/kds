package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.UnsafeKType
import kotlin.reflect.KType
import kotlin.reflect.typeOf


interface KTypeDataStorage {
    /**
     * May be you would use inline [putWithKType]?
     */
    @UnsafeKType
    fun putWithKType(key: String, type: KType, value: Any?)

    /**
     * May be you would use inline []
     */
    @UnsafeKType
    fun <T> getWithKType(key: String, type: KType, default: () -> T): T
}


@DelicateKDSApi
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.putWithKType(key: String, value: T) =
    putWithKType(key, typeOf<T>(), value)

@DelicateKDSApi
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
inline fun <reified T> KTypeDataStorage.getWithKType(key: String, noinline default: () -> T) =
    getWithKType(key, typeOf<T>(), default)
