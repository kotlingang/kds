package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
inline fun KBlockingDataStorage.mutateBlocking(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    commitBlocking()
}
