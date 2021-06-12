package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.JsonElementDataStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


internal class KJsonElementDataStorage (
    private val json: Json
) : KPrimitiveBundleDataStorage(), JsonElementDataStorage {
    @RawSetterGetter
    override fun putJsonElement(key: String, value: JsonElement) {
        putString(key, json.encodeToString(value))
    }

    @RawSetterGetter
    override fun getJsonElement(key: String): JsonElement? {
        return json.decodeFromString(string = getString(key) ?: return null)
    }
}
