package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter


interface PrimitiveDataStorage : StringDataStorage {
    @RawSetterGetter
    override fun putString(key: String, value: String)
    @RawSetterGetter
    fun putInt(key: String, value: Int) = putString(key, "$value")
    @RawSetterGetter
    fun putLong(key: String, value: Long) = putString(key, "$value")
    @RawSetterGetter
    fun putFloat(key: String, value: Float) = putString(key, "$value")
    @RawSetterGetter
    fun putDouble(key: String, value: Double) = putString(key, "$value")
    @RawSetterGetter
    fun putBoolean(key: String, value: Boolean) = putString(key, "$value")

    @RawSetterGetter
    override fun getString(key: String): String?
    @RawSetterGetter
    fun getInt(key: String): Int? = getString(key)?.toIntOrNull()
    @RawSetterGetter
    fun getLong(key: String): Long? = getString(key)?.toLongOrNull()
    @RawSetterGetter
    fun getFloat(key: String): Float? = getString(key)?.toFloatOrNull()
    @RawSetterGetter
    fun getDouble(key: String): Double? = getString(key)?.toDoubleOrNull()
    @RawSetterGetter
    fun getBoolean(key: String): Boolean? = getString(key)?.toBooleanStrictOrNull()
}
