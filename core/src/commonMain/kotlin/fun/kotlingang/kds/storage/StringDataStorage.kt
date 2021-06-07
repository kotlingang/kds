package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter


public interface StringDataStorage {
    @RawSetterGetter
    public fun putString(key: String, value: String)

    @RawSetterGetter
    public fun getString(key: String): String?
}
