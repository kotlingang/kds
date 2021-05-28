package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KAsyncDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
inline fun KAsyncDataStorage.mutate(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    launchCommit()
}
