package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.storage.CommittableStorage


public inline fun <T : CommittableStorage> T.mutateBlocking(crossinline block: T.() -> Unit) {
    block()
    commitBlocking()
}
