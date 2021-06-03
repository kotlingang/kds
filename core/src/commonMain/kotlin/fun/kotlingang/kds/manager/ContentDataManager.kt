package `fun`.kotlingang.kds.manager


interface ContentDataManager {
    fun loadDataBlocking(): String
    fun saveDataBlocking(text: String)
}
