package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KJsonAsyncDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
suspend inline fun KJsonAsyncDataStorage.mutateCommit(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    commit()
}
