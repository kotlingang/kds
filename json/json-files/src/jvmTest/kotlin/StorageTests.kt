import Storage.launchesCount
import Storage.list
import Storage.random2
import `fun`.kotlingang.kds.KFileDataStorage
import `fun`.kotlingang.kds.annotation.ExperimentalKDSApi
import `fun`.kotlingang.kds.delegate.property
import `fun`.kotlingang.kds.delegate.storageList
import `fun`.kotlingang.kds.delegate.storageMap
import `fun`.kotlingang.kds.mutate.mutate
import `fun`.kotlingang.kds.mutate.mutateBlocking
import `fun`.kotlingang.kds.mutate.mutateCommit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random


@OptIn(ExperimentalKDSApi::class)
object Storage : KFileDataStorage() {
    val map by storageMap<String, Int>()
    val list by storageList<String>()

    private val randomDelegate = property { Random.nextLong() }
    var random by randomDelegate

    var random2 by property { Random.nextInt() }

    var launchesCount by property { 0 }

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
        println("Launches count: ${++launchesCount}")
        println("Random value: $random2")

        println("ListBefore: $list")

        list += "Element"

        println("List: $list")


        Storage.mutateCommit {
            mutableList += "AAA"
        }
    }

    @Test
    fun storageListMapTest() = Storage.mutateBlocking {
        list += "A"
        map.compute("launches") { _, v -> (v ?: 0) + 1 }
    }
}
