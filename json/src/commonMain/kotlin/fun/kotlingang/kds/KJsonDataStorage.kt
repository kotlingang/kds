package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.annotation.InternalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.JsonElementDataStorage
import `fun`.kotlingang.kds.storage.SerializableDataStorage
import `fun`.kotlingang.kds.sync.platformSynchronized
import `fun`.kotlingang.kds.optional.Optional
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KType


public open class KJsonDataStorage (
    final override val json: Json,
    private val storage: JsonElementDataStorage
) : SerializableDataStorage {

    /**
     * Any mutation/iteration/etc should be wrapped with synchronization
     */
    @DelicateKDSApi
    protected val references: MutableMap<String, Pair<KSerializer<*>, Any?>> = mutableMapOf()

    @DelicateKDSApi
    @OptIn(InternalKDSApi::class)
    protected fun encodeReferences(): Map<String, JsonElement> = platformSynchronized(lock = this) {
        references.mapValues { (_, v) ->
            val (serializer, value) = v
            encodeUnchecked(serializer, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> encodeUnchecked(serializer: KSerializer<T>, value: Any?) =
        json.encodeToJsonElement(serializer, value as T)

    @OptIn(DelicateKDSApi::class, InternalKDSApi::class)
    @RawSetterGetter
    final override fun <T> putSerializable(key: String, serializer: KSerializer<T>, value: T) {
        val element = json.encodeToJsonElement(serializer, value)
        platformSynchronized(lock = this) {
            references[key] = serializer to value
            storage.putJsonElement(key, element)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(DelicateKDSApi::class, InternalKDSApi::class)
    @RawSetterGetter
    final override fun <T> getSerializable(key: String, serializer: KSerializer<T>): Optional<T> =
        platformSynchronized(lock = this) result@ {
            Optional.Value (
                if(references.containsKey(key)) {
                    references[key]?.second as T
                } else {
                    json.decodeFromJsonElement (
                        serializer, element = storage.getJsonElement(key)
                            ?: return@result Optional.NotPresent
                    ).also { value ->
                        references[key] = serializer to value
                    }
                }
            )
        }

    @RawSetterGetter
    @UnsafeKType
    final override fun putWithKType(key: String, type: KType, value: Any?): Unit =
        putSerializable(key, json.serializersModule.serializer(type), value)

    @Suppress("UNCHECKED_CAST")
    @RawSetterGetter
    @UnsafeKType
    final override fun <T> getWithKType(key: String, type: KType): Optional<T> =
        getSerializable(key, json.serializersModule.serializer(type) as KSerializer<T>)
}
