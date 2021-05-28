@file:Suppress("MemberVisibilityCanBePrivate")

package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.composition.AutoSaveController
import `fun`.kotlingang.kds.composition.JsonReferencesProxy
import `fun`.kotlingang.kds.extensions.any.unit
import `fun`.kotlingang.kds.manager.BlockingDataManager
import `fun`.kotlingang.kds.mutate.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


open class KBlockingDataStorage (
    val json: Json = Json,
    private val manager: BlockingDataManager
) {
    @DelicateKDSApi
    val autoSaveController = AutoSaveController()
    @OptIn(DelicateKDSApi::class)
    val autoSave by autoSaveController::autoSave

    /**
     * [mutate], [mutateBlocking], [mutateCommit] should be used instead
     */
    @DelicateKDSApi
    inline fun withoutSave(crossinline block: () -> Unit) {
        autoSaveController.turnOff()
        block()
        autoSaveController.tryTurnOn()
    }

    protected open fun getOrLoadData() = manager.loadDataBlocking().parseData().toMutableMap()

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
    fun <T> set(name: String, serializer: KSerializer<T>, value: T) {
        dataProxy.set(name, serializer, value)
        performAutoSave()
    }

    @DelicateKDSApi
    inline fun <reified T> set(name: String, value: @Serializable T) =
        set(name, json.serializersModule.serializer(), value)

    @DelicateKDSApi
    fun <T> get(name: String, serializer: KSerializer<T>, default: () -> T) =
        dataProxy.get(name, serializer, default)

    @DelicateKDSApi
    inline fun <reified T> get(name: String, noinline default: () -> T) =
        get(name, json.serializersModule.serializer(), default)

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
     * For [KBlockingDataStorage] auto save way is blocking way while
     * for [KAsyncDataStorage] auto save way is async way
     */
    protected open fun performAutoSave() {
        if(autoSave)
            commitBlocking()
    }

    protected fun String.parseData() = json.decodeFromString<Map<String, JsonElement>>(string = this)
    protected fun Map<String, JsonElement>.encodeData() = json.encodeToString(value = this)
}
