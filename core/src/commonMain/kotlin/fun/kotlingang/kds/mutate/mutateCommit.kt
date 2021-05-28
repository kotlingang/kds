package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KAsyncDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
suspend inline fun KAsyncDataStorage.mutateCommit(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    commit()
}
