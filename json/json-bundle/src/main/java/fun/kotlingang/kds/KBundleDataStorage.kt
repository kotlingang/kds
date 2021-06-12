package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import android.os.Bundle
import kotlinx.serialization.json.Json


public open class KBundleDataStorage private constructor (
    json: Json,
    private val storage: KJsonElementDataStorage
): KJsonDataStorage(json, storage), PrimitiveDataStorage, InstanceStateManager {
    public constructor (
        json: Json = Json
    ) : this(json, KJsonElementDataStorage(json))

    final override fun restoreInstanceState(bundle: Bundle?) {
        storage.restoreInstanceState(bundle)
    }
    final override fun saveInstanceState(bundle: Bundle) {
        storage.saveInstanceState(bundle)
    }

    @RawSetterGetter
    final override fun putString(key: String, value: String) {
        storage.putString(key, value)
    }

    @RawSetterGetter
    final override fun putInt(key: String, value: Int) {
        storage.putInt(key, value)
    }

    @RawSetterGetter
    final override fun putLong(key: String, value: Long) {
        storage.putLong(key, value)
    }

    @RawSetterGetter
    final override fun putFloat(key: String, value: Float) {
        storage.putFloat(key, value)
    }

    @RawSetterGetter
    final override fun putDouble(key: String, value: Double) {
        storage.putDouble(key, value)
    }

    @RawSetterGetter
    final override fun putBoolean(key: String, value: Boolean) {
        storage.putBoolean(key, value)
    }

    @RawSetterGetter
    final override fun getString(key: String): String? {
        return storage.getString(key)
    }

    @RawSetterGetter
    final override fun getInt(key: String): Int? {
        return storage.getInt(key)
    }

    @RawSetterGetter
    final override fun getLong(key: String): Long? {
        return storage.getLong(key)
    }

    @RawSetterGetter
    final override fun getFloat(key: String): Float? {
        return storage.getFloat(key)
    }

    @RawSetterGetter
    final override fun getDouble(key: String): Double? {
        return storage.getDouble(key)
    }

    @RawSetterGetter
    final override fun getBoolean(key: String): Boolean? {
        return storage.getBoolean(key)
    }

    @OptIn(DelicateKDSApi::class, RawSetterGetter::class)
    override fun commitBlocking() {
        encodeReferences().forEach { (k, v) ->
            storage.putJsonElement(k, v)
        }
    }

    final override fun setupBlocking() {
        super.setupBlocking()
    }
}
