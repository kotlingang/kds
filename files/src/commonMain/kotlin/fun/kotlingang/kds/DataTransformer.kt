package `fun`.kotlingang.kds


interface DataTransformer {
    fun encode(data: Map<String, String>): String
    fun decode(text: String): Map<String, String>
}
