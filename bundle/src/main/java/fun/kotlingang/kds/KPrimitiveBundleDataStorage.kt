package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.extensions.bundle.contains
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import android.os.Bundle


public open class KPrimitiveBundleDataStorage : PrimitiveDataStorage, InstanceStateManager {
    private val bundle = Bundle()

    override fun restoreInstanceState(bundle: Bundle?) {
        if(bundle != null)
            this.bundle.putAll(bundle)
    }
    override fun saveInstanceState(bundle: Bundle) {
        bundle.putAll(this.bundle)
    }

    @RawSetterGetter
    final override fun putString(key: String, value: String) {
        bundle.putString(key, value)
    }

    @RawSetterGetter
    final override fun putInt(key: String, value: Int) {
        bundle.putInt(key, value)
    }

    @RawSetterGetter
    final override fun putLong(key: String, value: Long) {
        bundle.putLong(key, value)
    }

    @RawSetterGetter
    final override fun putFloat(key: String, value: Float) {
        bundle.putFloat(key, value)
    }

    @RawSetterGetter
    final override fun putDouble(key: String, value: Double) {
        bundle.putDouble(key, value)
    }

    @RawSetterGetter
    final override fun putBoolean(key: String, value: Boolean) {
        bundle.putBoolean(key, value)
    }

    @RawSetterGetter
    final override fun getString(key: String): String? =
        if(key in bundle)
            bundle.getString(key)
        else 
            null

    @RawSetterGetter
    final override fun getInt(key: String): Int? =
        if(key in bundle) 
            bundle.getInt(key) 
        else
            null

    @RawSetterGetter
    final override fun getLong(key: String): Long? =
        if(key in bundle)
            bundle.getLong(key)
        else
            null

    @RawSetterGetter
    final override fun getFloat(key: String): Float? =
        if(key in bundle) 
            bundle.getFloat(key) 
        else 
            null

    @RawSetterGetter
    final override fun getDouble(key: String): Double? =
        if(key in bundle)
            bundle.getDouble(key)
        else 
            null

    @RawSetterGetter
    final override fun getBoolean(key: String): Boolean? =
        if(key in bundle)
            bundle.getBoolean(key)
        else 
            null
}
