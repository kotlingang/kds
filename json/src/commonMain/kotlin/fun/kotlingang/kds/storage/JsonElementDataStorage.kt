package `fun`.kotlingang.kds.storage

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import kotlinx.serialization.json.JsonElement


public interface JsonElementDataStorage {
    @RawSetterGetter
    public fun putJsonElement(key: String, value: JsonElement)

    @RawSetterGetter
    public fun getJsonElement(key: String): JsonElement?
}
