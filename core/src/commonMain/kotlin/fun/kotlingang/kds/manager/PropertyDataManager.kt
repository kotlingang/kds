package `fun`.kotlingang.kds.manager


interface PropertyDataManager {
    fun put(key: String, text: String)
    fun get(key: String): String?
}
