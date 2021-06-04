package `fun`.kotlingang.kds.storage


interface AsyncCommittableStorage : CommittableStorage {
    suspend fun setup()

    fun launchCommit()
    suspend fun commit()
}
