package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.annotation.ExperimentalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlin.reflect.KType


/**
 * This API is experimental since there are no ideas for clear implementation of it (exactly map)
 * You can write your ideas to #16
 * After some testing time we probably will understand should we
 * keep this types in library or not
 */
@ExperimentalKDSApi
@OptIn(UnsafeKType::class, RawSetterGetter::class)
public class StorageMap<K, V> @UnsafeKType constructor (
    private val storageKey: String,
    private val storage: KTypeDataStorage,
    private val mapSource: MutableMap<K, V>,
    private val mapType: KType,
) : MutableMap<K, V> by SavableMap(mapSource, saveAction = {
    storage.putWithKType(storageKey, mapType, mapSource)
})
