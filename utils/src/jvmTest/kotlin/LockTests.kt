import com.kotlingang.kds.SpinLocker
import com.kotlingang.kds.runTestBlocking
import com.kotlingang.kds.withLock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import kotlin.concurrent.thread

class LockerTests {
    @Test
    fun simpleTest() = runBlocking {
        val locker = SpinLocker()
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