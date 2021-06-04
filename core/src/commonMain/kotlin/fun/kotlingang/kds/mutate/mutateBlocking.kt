package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.CommittableStorage


inline fun CommittableStorage.mutateBlocking(crossinline block: () -> Unit) {
    block()
    commitBlocking()
}
