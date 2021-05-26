package `fun`.kotlingang.kds.manager

interface BlockingDataManager {
    fun loadDataBlocking(): String
    fun saveDataBlocking(text: String)
}

interface AsyncDataManager : BlockingDataManager {
    suspend fun loadData(): String
    suspend fun saveData(text: String)
}
