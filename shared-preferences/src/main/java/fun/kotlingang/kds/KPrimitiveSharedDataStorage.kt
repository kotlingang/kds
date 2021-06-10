package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.InternalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import android.content.Context
import android.content.SharedPreferences


public open class KPrimitiveSharedDataStorage (
    prefsName: String = "data",
    prefsMode: Int = Context.MODE_PRIVATE
) : PrimitiveDataStorage {

    @OptIn(InternalKDSApi::class)
    private val preferences by lazy {
        KDS.context.getSharedPreferences(prefsName, prefsMode)
    }

    private fun SharedPreferences.edit(save: Boolean = true, block: SharedPreferences.Editor.() -> Unit) {
        with(edit().apply(block)) {
            if(save)
                apply()
        }
    }

    @RawSetterGetter
    final override fun putString(key: String, value: String) {
        preferences.edit {
            putString(key, value)
        }
    }

    @RawSetterGetter
    final override fun putInt(key: String, value: Int) {
        preferences.edit {
            putInt(key, value)
        }
    }

    @RawSetterGetter
    final override fun putLong(key: String, value: Long) {
        preferences.edit {
            putLong(key, value)
        }
    }

    @RawSetterGetter
    final override fun putFloat(key: String, value: Float) {
        preferences.edit {
            putFloat(key, value)
        }
    }

    @RawSetterGetter
    final override fun putDouble(key: String, value: Double) {
        preferences.edit {
            putLong(key, java.lang.Double.doubleToLongBits(value))
        }
    }

    @RawSetterGetter
    final override fun putBoolean(key: String, value: Boolean) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    @RawSetterGetter
    final override fun getString(key: String): String? = preferences.getString(key, null)

    @RawSetterGetter
    final override fun getInt(key: String): Int? =
        if(key in preferences)
            preferences.getInt(key, 0)
        else
            null

    @RawSetterGetter
    final override fun getLong(key: String): Long? =
        if(key in preferences)
            preferences.getLong(key, 0)
        else
            null

    @RawSetterGetter
    final override fun getFloat(key: String): Float? =
        if(key in preferences)
            preferences.getFloat(key, 0f)
        else
            null

    @RawSetterGetter
    final override fun getDouble(key: String): Double? =
        if(key in preferences) {
            java.lang.Double.longBitsToDouble(preferences.getLong(key, 0))
        } else null

    @RawSetterGetter
    final override fun getBoolean(key: String): Boolean? =
        if(key in preferences)
            preferences.getBoolean(key, false)
        else
            null
}
