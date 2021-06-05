package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.JsonElementDataStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


internal class JsonElementLocalDataStorage (
    private val json: Json
) : JsonElementDataStorage {
    @RawSetterGetter
    override fun putJsonElement(key: String, value: JsonElement) =
        KStringLocalDataStorage.putString(key, json.encodeToString(value))

    @RawSetterGetter
    override fun getJsonElement(key: String): JsonElement? =
        KStringLocalDataStorage.getString(key)?.let(json::decodeFromString)
}
