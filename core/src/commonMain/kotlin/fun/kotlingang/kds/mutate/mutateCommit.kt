package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.annotation.DelicateKDSApi
import `fun`.kotlingang.kds.storage.AsyncCommittableStorage


suspend inline fun AsyncCommittableStorage.mutateCommit(crossinline block: () -> Unit) {
    block()
    commit()
}
