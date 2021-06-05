package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter


interface StringDataStorage {
    @RawSetterGetter
    fun putString(key: String, value: String)

    @RawSetterGetter
    fun getString(key: String): String?
}
