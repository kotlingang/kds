package `fun`.kotlingang.kds.data_manager


interface AsyncContentDataManager : BlockingContentDataManager {
    suspend fun loadData(): String
    suspend fun saveData(text: String)
}
