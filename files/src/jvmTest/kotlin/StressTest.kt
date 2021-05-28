import `fun`.kotlingang.kds.delegate.property
import kotlinx.coroutines.*
import org.junit.Test


var stressTest by storage.property { "" }

@DelicateCoroutinesApi
class StressTest {
    @Test
    fun stressTest() = runBlocking {
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
