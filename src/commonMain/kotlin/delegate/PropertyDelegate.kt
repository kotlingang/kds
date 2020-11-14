package com.kotlingang.kds.delegate

import com.kotlingang.kds.KDataStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty


class PropertyDelegate<T> internal constructor(private val serializer: KSerializer<T>, private val default: T) {
    operator fun getValue(storage: KDataStorage, property: KProperty<*>): T {
        val element = storage.data[property.name] ?: return default
        return Json.decodeFromJsonElement(serializer, element)
    }

    operator fun setValue(storage: KDataStorage, property: KProperty<*>, value: T) {
        val element = Json.encodeToJsonElement(serializer, value)
        storage.data[property.name] = element

        storage.launchCommit()
    }
}
