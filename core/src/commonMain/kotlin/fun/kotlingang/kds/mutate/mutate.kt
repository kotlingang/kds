package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.AsyncCommittableStorage


public inline fun <T : AsyncCommittableStorage> T.mutate(crossinline block: T.() -> Unit) {
    block()
    launchCommit()
}
