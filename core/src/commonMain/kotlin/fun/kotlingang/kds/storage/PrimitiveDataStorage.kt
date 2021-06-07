package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter


public interface PrimitiveDataStorage : StringDataStorage {
    @RawSetterGetter
    override fun putString(key: String, value: String)
    @RawSetterGetter
    public fun putInt(key: String, value: Int): Unit = putString(key, "$value")
    @RawSetterGetter
    public fun putLong(key: String, value: Long): Unit = putString(key, "$value")
    @RawSetterGetter
    public fun putFloat(key: String, value: Float): Unit = putString(key, "$value")
    @RawSetterGetter
    public fun putDouble(key: String, value: Double): Unit = putString(key, "$value")
    @RawSetterGetter
    public fun putBoolean(key: String, value: Boolean): Unit = putString(key, "$value")

    @RawSetterGetter
    override fun getString(key: String): String?
    @RawSetterGetter
    public fun getInt(key: String): Int? = getString(key)?.toIntOrNull()
    @RawSetterGetter
    public fun getLong(key: String): Long? = getString(key)?.toLongOrNull()
    @RawSetterGetter
    public fun getFloat(key: String): Float? = getString(key)?.toFloatOrNull()
    @RawSetterGetter
    public fun getDouble(key: String): Double? = getString(key)?.toDoubleOrNull()
    @RawSetterGetter
    public fun getBoolean(key: String): Boolean? = getString(key)?.toBooleanStrictOrNull()
}
