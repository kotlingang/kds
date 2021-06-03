package `fun`.kotlingang.kds.manager


interface AsyncContentDataManager : ContentDataManager {
    suspend fun loadData(): String
    suspend fun saveData(text: String)
}
