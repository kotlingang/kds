import `fun`.kotlingang.kds.delegate.property
import kotlinx.coroutines.*
import org.junit.Test


var stressTest by Storage.property { "" }

@DelicateCoroutinesApi
class StressTest {
    @Test
    fun stressTest() = runBlocking {
        withContext(Dispatchers.IO) {
            (1..100_000).map { i ->
                stressTest = "S".repeat(i)
                println(i)
            }
        }
        Storage.commit()
        println("Committed: $stressTest (${stressTest.length})")
        delay(2_000)
    }
}
