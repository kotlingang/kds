package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.AsyncCommittableStorage


public suspend inline fun <T : AsyncCommittableStorage> T.mutateCommit(crossinline block: T.() -> Unit) {
    block()
    commit()
}
