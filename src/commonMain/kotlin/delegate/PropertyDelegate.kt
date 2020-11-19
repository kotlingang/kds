@file:Suppress("UNCHECKED_CAST")

package com.kotlingang.kds.delegate

import com.kotlingang.kds.KDataStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty


class PropertyDelegateProvider<T> internal constructor(
        private val serializer: KSerializer<T>,
        private val lazyDefault: () -> T
) {
    operator fun provideDelegate(storage: KDataStorage, property: KProperty<*>) = PropertyDelegate(storage, property, serializer, lazyDefault)
}

class PropertyDelegate<T> internal constructor (
        private val storage: KDataStorage,
        private val property: KProperty<*>,
        private val serializer: KSerializer<T>,
        private val lazyDefault: () -> T
) {
    private val lazyValueProvider by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val reference = storage.getReference(property.name)

        // second check, but now thread-safe
        if(reference == null) {
            val element = storage.data[property.name]
            val value = element?.let { storage.json.decodeFromJsonElement(serializer, element) } ?: lazyDefault()
            storage.saveReference(property.name, value, serializer)
            return@lazy value
        } else {
            reference
        }
    }

    operator fun getValue(thisRef: KDataStorage, property: KProperty<*>): T {
        val reference = storage.getReference(property.name)

        // If there is reference then value was already gotten with this method
        // and we should return local copy for case if value is mutable and it was changed
        return (reference ?: lazyValueProvider) as T
    }

    operator fun setValue(thisRef: KDataStorage, property: KProperty<*>, value: T) {
        storage.saveReference(property.name, value, serializer)
        storage.launchCommit()
    }
}
