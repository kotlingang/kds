package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.JsonElementDataStorage
import `fun`.kotlingang.kds.storage.PrimitiveDataStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


internal class KJsonElementSharedDataStorage (
    private val json: Json,
    private val storage: KPrimitiveSharedDataStorage
) : JsonElementDataStorage, PrimitiveDataStorage by storage {
    @RawSetterGetter
    override fun putJsonElement(key: String, value: JsonElement) =
        storage.putString(key, json.encodeToString(value))

    @RawSetterGetter
    override fun getJsonElement(key: String): JsonElement? {
        return json.decodeFromString (
            string = storage.getString(key) ?: return null
        )
    }
}
