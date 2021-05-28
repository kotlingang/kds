package `fun`.kotlingang.kds.data_manager


interface BlockingContentDataManager {
    fun loadDataBlocking(): String
    fun saveDataBlocking(text: String)
}
