@file:Suppress("MemberVisibilityCanBePrivate")

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.components.AutoSaveController
import `fun`.kotlingang.kds.components.JsonReferencesProxy
import `fun`.kotlingang.kds.extensions.any.unit
import `fun`.kotlingang.kds.manager.ContentDataManager
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import `fun`.kotlingang.kds.storage.SerializableDataStorage
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KType


open class KJsonBlockingDataStorage (
    val json: Json,
    private val manager: ContentDataManager
): SerializableDataStorage, KTypeDataStorage {
    @DelicateKDSApi
    val autoSaveController = AutoSaveController()
    @OptIn(DelicateKDSApi::class)
    val autoSave by autoSaveController::autoSave

    protected open fun getOrLoadData() = manager.loadDataBlocking().parseData()

    private val dataProxy by lazy {
        JsonReferencesProxy(json, getOrLoadData())
    }

    protected val data get() = dataProxy.publicData

    fun loadDataBlocking() = dataProxy.unit

    /**
     * Stores all references to data in case references were changed
     */
    @DelicateKDSApi
    fun applyMutations() = dataProxy.applyMutations()

    @DelicateKDSApi
    override fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T) {
        dataProxy.set(key, serializer, value)
        performAutoSave()
    }

    @DelicateKDSApi
    @OptIn(DelicateKDSApi::class, UnsafeKType::class)
    override fun putWithKType(key: String, type: KType, value: Any?) =
        putSerializable(key, json.serializersModule.serializer(type), value)

    @DelicateKDSApi
    override fun <T> getSerializable(key: String, serializer: KSerializer<T>, default: () -> T) =
        dataProxy.get(key, serializer, default)

    @UnsafeKType
    @Suppress("UNCHECKED_CAST")
    @DelicateKDSApi
    override fun <T> getWithKType(key: String, type: KType, default: () -> T): T =
        getSerializable(key, json.serializersModule.serializer(type) as KSerializer<T>, default)

    fun exists(name: String) = dataProxy.exists(name)

    fun clear(name: String) {
        dataProxy.clear(name)
        performAutoSave()
    }

    fun clear() {
        dataProxy.clear()
        performAutoSave()
    }

    fun commitBlocking() = manager.saveDataBlocking(data.encodeData())

    /**
     * For [KJsonBlockingDataStorage] auto save way is blocking way while
     * for [KJsonAsyncDataStorage] auto save way is async way
     */
    protected open fun performAutoSave() {
        if(autoSave)
            commitBlocking()
    }

    protected fun String.parseData() = json.decodeFromString<Map<String, JsonElement>>(string = this)
    protected fun Map<String, JsonElement>.encodeData() = json.encodeToString(value = this)
}
