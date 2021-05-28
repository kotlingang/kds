package `fun`.kotlingang.kds.composition

import `fun`.kotlingang.kds.sync.platformSynchronized
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer


/**
 * Entity used to store map of [String] to [JsonElement], but
 * with additional references storage for handling mutations
 */
internal class JsonReferencesProxy (
    private val json: Json,
    data: Map<String, JsonElement>
) {
    private val data = data.toMutableMap()
    val publicData get() = platformSynchronized(lock = this) { data.toMap() }

    private val references = mutableMapOf<String, Pair<KSerializer<*>, *>>()

    fun <T> get (
        key: String,
        serializer: KSerializer<T>,
        default: () -> T
    ) = platformSynchronized(lock = this) {
        if(key in references)
            @Suppress("UNCHECKED_CAST")
            return@platformSynchronized references[key]!!.second as T

        val element = data[key] ?: return@platformSynchronized default().also {
            references[key] = serializer to it
            data[key] = json.encodeToJsonElement(serializer, it)
        }

        return@platformSynchronized json.decodeFromJsonElement(serializer, element).also {
            references[key] = serializer to it
        }
    }

    fun <T> set (
        key: String,
        serializer: KSerializer<T>,
        value: T
    ) = platformSynchronized(lock = this) {
        references[key] = serializer to value
        data[key] = json.encodeToJsonElement(serializer, value)
    }

    fun exists(key: String) = key in data

    fun clear() = platformSynchronized(lock = this) {
        references.clear()
        data.clear()
    }

    fun clear(key: String) = platformSynchronized(lock = this) {
        references.remove(key)
        data.remove(key)
    }

    fun applyMutations() = platformSynchronized(lock = this) {
        @Suppress("UNCHECKED_CAST")
        fun <T> encodeUnsafe(serializer: KSerializer<T>, value: Any?) =
            json.encodeToJsonElement(serializer, value as T)

        for((k, v) in references) {
            val (serializer, value) = v
            data[k] = encodeUnsafe(serializer, value)
        }
    }
}
