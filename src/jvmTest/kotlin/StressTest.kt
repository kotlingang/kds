import `fun`.kotlingang.kds.delegate.property
import `fun`.kotlingang.kds.test.launchTest
import kotlinx.coroutines.*
import org.junit.Test


var stressTest by storage.property { "" }

@DelicateCoroutinesApi
class StressTest {
    @Test
    fun stressTest() = GlobalScope.launchTest {
        withContext(Dispatchers.IO) {
            (1..1_000).map { i ->
                async {
                    stressTest = "S".repeat(i)
                    println(i)
                }
            }.awaitAll()
        }
        storage.commit()
        println("Committed: $stressTest (${stressTest.length})")
        delay(2_000)
    }
}
