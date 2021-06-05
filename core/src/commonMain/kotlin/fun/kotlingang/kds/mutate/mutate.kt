package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.AsyncCommittableStorage


inline fun <T : AsyncCommittableStorage> T.mutate(crossinline block: T.() -> Unit) {
    block()
    launchCommit()
}
