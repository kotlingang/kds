package `fun`.kotlingang.kds.storage


public interface AsyncCommittableStorage : CommittableStorage {
    override fun commitBlocking()
    public suspend fun setup() {}

    override fun setupBlocking() {}
    public fun launchCommit()
    public suspend fun commit()
}
