package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.optional.Optional
import kotlin.reflect.KType
import kotlin.reflect.typeOf


public interface KTypeDataStorage {
    /**
     * May be you would use inline [putWithKType]?
     */
    @RawSetterGetter
    @UnsafeKType
    public fun putWithKType(key: String, type: KType, value: Any?)

    /**
     * May be you would use inline [getWithKType]?
     */
    @RawSetterGetter
    @UnsafeKType
    public fun <T> getWithKType(key: String, type: KType): Optional<T>
}

@RawSetterGetter
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.putWithKType(key: String, value: T): Unit =
    putWithKType(key, typeOf<T>(), value)

@RawSetterGetter
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.getWithKType(key: String): Optional<T> =
    getWithKType(key, typeOf<T>())
