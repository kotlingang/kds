package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KJsonBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


@OptIn(DelicateKDSApi::class)
inline fun KJsonBlockingDataStorage.mutateBlocking(crossinline block: () -> Unit) {
    withoutSave(block)
    applyMutations()
    commitBlocking()
}
