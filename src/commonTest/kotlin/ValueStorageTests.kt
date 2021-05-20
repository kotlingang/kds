import `fun`.kotlingang.kds.value.KValueStorage
import `fun`.kotlingang.kds.runTestBlocking
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlin.test.Test


@DelicateCoroutinesApi
class ValueStorageTests {
    @Test
    fun simpleValueStorageTest() = GlobalScope.runTestBlocking {
        val valueStorage = KValueStorage(default = 0, coroutineScope = this)
        var launchNumber by valueStorage

        // Required for JS targets
        valueStorage.awaitLoading()
        println(++launchNumber)
    }
}
