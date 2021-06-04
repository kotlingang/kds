package `fun`.kotlingang.kds.storage


interface CommittableStorage {
    fun setupBlocking() {}

    fun commitBlocking()
}
