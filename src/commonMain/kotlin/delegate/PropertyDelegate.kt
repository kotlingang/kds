package com.kotlingang.kds.delegate

import com.kotlingang.kds.KDataStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty


class PropertyDelegate<T> internal constructor(private val serializer: KSerializer<T>, private val default: T) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(storage: KDataStorage, property: KProperty<*>): T {
        val reference = storage.getReference(property.name)

        // If there is reference then value was already gotten with this method
        // and we should return local copy for case if value is mutable and it was changed
        return if(reference == null) {
            val element = storage.data[property.name]
            val value = element?.let { Json.decodeFromJsonElement(serializer, element) } ?: default
            storage.saveReference(property.name, value, serializer)
            value
        } else {
            reference as T
        }
    }

    operator fun setValue(storage: KDataStorage, property: KProperty<*>, value: T) {
        storage.saveReference(property.name, value, serializer)
        storage.launchCommit()
    }
}
