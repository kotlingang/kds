package `fun`.kotlingang.kds.delegate

import `fun`.kotlingang.kds.annotation.ExperimentalKDSApi
import `fun`.kotlingang.kds.annotation.UnsafeKType
import `fun`.kotlingang.kds.mutate.StorageSet
import `fun`.kotlingang.kds.storage.KTypeDataStorage
import kotlin.reflect.typeOf


/**
 * @see [StorageSet]
 */
@ExperimentalKDSApi
@OptIn(ExperimentalStdlibApi::class, UnsafeKType::class)
public inline fun <reified T> KTypeDataStorage.storageSet (
    crossinline default: () -> MutableSet<T> = { mutableSetOf() }
): DelegateProvider<Any?, KDSProperty<MutableSet<T>>> =
    DelegateProvider { _, property ->
        property {
            StorageSet(property.name, storage = this, default(), typeOf<MutableSet<T>>())
        }
    }
