package `fun`.kotlingang.kds

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


internal class JsonDataTransformer (
    private val json: Json
) : DataTransformer {
    override fun encode(data: Map<String, String>) = json.encodeToString(data)
    override fun decode(text: String): Map<String, String> = json.decodeFromString(text)
}