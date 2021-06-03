package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KJsonAsyncDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
inline fun KJsonAsyncDataStorage.mutate(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    launchCommit()
}
