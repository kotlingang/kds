package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.AsyncCommittableStorage


inline fun AsyncCommittableStorage.mutate(crossinline block: () -> Unit) {
    block()
    launchCommit()
}
