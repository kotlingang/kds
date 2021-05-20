import `fun`.kotlingang.kds.PlatformLocker
import `fun`.kotlingang.kds.withLock
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LockerTests {
    @Test
    fun simpleTest() = runBlocking {
        val locker = PlatformLocker()
        launch {
            locker.withLock {
                println("First lock")
                Thread.sleep(5000)
            }
        }
        delay(1000)
        locker.withLock {
            println("Second lock")
        }
        Thread.sleep(10000)
    }
}