import `fun`.kotlingang.kds.KFileDataStorage
import `fun`.kotlingang.kds.delegate.property
import `fun`.kotlingang.kds.mutate.mutate
import `fun`.kotlingang.kds.mutate.mutateBlocking
import `fun`.kotlingang.kds.mutate.mutateCommit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random


object Storage : KFileDataStorage() {
    private val randomDelegate = property { Random.nextLong() }
    var random by randomDelegate

    var random2 by property { Random.nextInt() }

    var launchesCount by property { 0 }

    var list by property { mutableListOf<String>() }
    val mutableList by property { mutableListOf<String>() }
}


@DelicateCoroutinesApi
class StorageTests {
    @Test
    fun simpleStorageTest() = runBlocking {
        println("Awaiting loading")
        Storage.setupBlocking()
        Storage.setup()
        println("Awaited loading")

        Storage.mutate {
            println("Launches count: ${++launchesCount}")
            println("Random value: $random2")

            println("ListBefore: $list")

            mutate {
                list += "Element"
            }

            println("List: $list")
        }

        Storage.commit()
    }

    @Test
    fun mutableStorageTest() = Storage.mutateBlocking {
        mutableList += "Test"
    }
}
