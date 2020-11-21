@file:Suppress("UNCHECKED_CAST")

package com.kotlingang.kds.delegate

import com.kotlingang.kds.KDataStorage
import com.kotlingang.kds.synchronized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class KDataStorageProperty<T> internal constructor(
        private val serializer: KSerializer<T>,
        private val lazyDefault: () -> T
) {

    private var delegated: Pair<KDataStorage, KProperty<*>>? = null

    fun clear() {
        val (storage, property) = delegated ?: error("There is no property that was delegated")
        storage.clear(property.name)
    }

    operator fun provideDelegate(storage: KDataStorage, property: KProperty<*>): KDataStoragePropertyDelegate<T> {
        delegated = storage to property
        return KDataStoragePropertyDelegate(serializer, lazyDefault)
    }
}

class KDataStoragePropertyDelegate<T> internal constructor (
        private val serializer: KSerializer<T>,
        private val lazyDefault: () -> T
) {
    operator fun getValue(storage: KDataStorage, property: KProperty<*>): T = synchronized(storage) {
        val reference = storage.getReference(property.name)

        // If there is reference then value was already gotten with this method
        // and we should return local copy for case if value is mutable and it was changed
        return if(reference == null) {
            val element = storage.data[property.name]
            val value = element?.let { storage.json.decodeFromJsonElement(serializer, element) } ?: lazyDefault()
            storage.saveReference(property.name, value, serializer)
            storage.launchCommit()
            value
        } else {
            reference
        } as T
    }

    operator fun setValue(storage: KDataStorage, property: KProperty<*>, value: T) = synchronized(storage) {
        storage.saveReference(property.name, value, serializer)
        storage.launchCommit()
    }
}
