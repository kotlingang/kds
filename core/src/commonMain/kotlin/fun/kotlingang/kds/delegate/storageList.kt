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
    crossinline default: () -> MutableList<T> = { mutableListOf() }
): DelegateProvider<Any?, KDSProperty<MutableList<T>>> =
    DelegateProvider { _, property ->
        property {
            StorageList(property.name, storage = this, default(), typeOf<MutableList<T>>())
        }
    }

