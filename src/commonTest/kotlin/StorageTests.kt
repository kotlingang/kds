import `fun`.kotlingang.kds.KDataStorage
import `fun`.kotlingang.kds.commitMutate
import `fun`.kotlingang.kds.mutate
import `fun`.kotlingang.kds.runTestBlocking
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.test.Test


@DelicateCoroutinesApi
class StorageTests {
    object Storage : KDataStorage(name = "Storage") {  // or KDataStorage("name") or KDataStorage({ path("...") })
        var random by property { Random.nextLong() }

        val random2Delegate = property { Random.nextInt() }
        var random2 by random2Delegate

        var launchesCount by property(0)
        var list by property(mutableListOf<String>())
        val mutableList by property(mutableListOf<String>())
    }

    @Test
    fun simpleStorageTest() = GlobalScope.runTestBlocking {
        println("Awaiting loading")
        Storage.commitMutate {
            println("Awaited loading")

            println("Launches count: ${++launchesCount}")
            println("Random value: $random")
            list += "Element"
            println("List saved")

            println("List: $list")
        }
    }

    @Test
    fun mutableStorageTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            awaitLoading()  // for JS
            mutableList += "Test"
            launchCommit()
        }
    }

    @Test
    fun clearTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            println(random2)
            random2Delegate.clear()
            println(random2)
        }
    }
}
