package `fun`.kotlingang.kds.storage


interface PrimitiveDataStorage {
    fun putInt(key: String, value: Int)
    fun putLong(key: String, value: Long)
    fun putFloat(key: String, value: Float)
    fun putDouble(key: String, value: Double)
    fun putBoolean(key: String, value: Boolean)
    fun putString(key: String, value: String)

    fun getInt(key: String, default: () -> Int): Int
    fun getLong(key: String, default: () -> Long): Long
    fun getFloat(key: String, default: () -> Float): Float
    fun getDouble(key: String, default: () -> Double): Double
    fun getBoolean(key: String, default: () -> Boolean): Boolean
    fun getString(key: String, default: () -> String): String
}
