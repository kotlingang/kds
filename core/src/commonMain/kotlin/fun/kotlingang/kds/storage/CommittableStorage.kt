package `fun`.kotlingang.kds.storage


public interface CommittableStorage {
    public fun setupBlocking() {}

    public fun commitBlocking()
}
