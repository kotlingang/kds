package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.annotation.ExperimentalKDSApi
import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlin.reflect.KType


/**
 * This API is experimental as [StorageMap]
 * You can write your ideas to #16
 * After some testing time we probably will understand should we
 * keep this types in library or not
 */
@ExperimentalKDSApi
@OptIn(UnsafeKType::class, RawSetterGetter::class)
public class StorageList<T> @UnsafeKType constructor (
    private val storageKey: String,
    private val storage: KTypeDataStorage,
    private val listSource: MutableList<T>,
    private val listType: KType
) : MutableList<T> by SavableList(listSource, saveAction = {
    storage.putWithKType(storageKey, listType, listSource)
})
