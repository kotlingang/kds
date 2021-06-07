package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.ExperimentalKDSApi
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.mutate.StorageList
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlin.reflect.typeOf


/**
 * @see [StorageList]
 */
@ExperimentalKDSApi
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.storageList (
    list: MutableList<T>
): DelegateProvider<Any?, KDSProperty<MutableList<T>>> =
    DelegateProvider { _, property ->
        property {
            StorageList(property.name, storage = this, list, typeOf<MutableList<T>>())
        }
    }

/**
 * @see [StorageList]
 */
@ExperimentalKDSApi
public inline fun <reified T> KTypeDataStorage.storageList (
    vararg elements: T
): DelegateProvider<Any?, KDSProperty<MutableList<T>>> = storageList(mutableListOf(*elements))

