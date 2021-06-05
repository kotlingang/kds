package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import kotlinx.serialization.json.JsonElement


interface JsonElementDataStorage {
    @RawSetterGetter
    fun putJsonElement(key: String, value: JsonElement)

    @RawSetterGetter
    fun getJsonElement(key: String): JsonElement?
}
