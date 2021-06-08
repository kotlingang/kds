package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.AsyncCommittableStorage
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import android.content.Context
import kotlinx.serialization.json.Json


public open class KSharedDataStorage internal constructor (
    json: Json,
    private val storage: KJsonElementSharedDataStorage
) : KJsonDataStorage(json, storage), PrimitiveDataStorage, AsyncCommittableStorage {
    public constructor (
        prefsName: String = "data",
        prefsMode: Int = Context.MODE_PRIVATE,
        json: Json = Json
    ) : this(json, KJsonElementSharedDataStorage(json, KPrimitiveSharedDataStorage(prefsName, prefsMode)))

    @OptIn(DelicateKDSApi::class, RawSetterGetter::class)
    final override fun commitBlocking(): Unit = encodeReferences()
        .forEach { (k, v) ->
            storage.putJsonElement(k, v)
        }

    // SharedPreferences.apply method is async
    override suspend fun commit(): Unit = commitBlocking()
    // SharedPreferences.apply method launches async saving
    override fun launchCommit(): Unit = commitBlocking()

    final override fun setupBlocking() {}
    final override suspend fun setup() {}

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
}
