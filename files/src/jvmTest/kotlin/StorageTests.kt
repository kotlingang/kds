import `fun`.kotlingang.kds.KFileDataStorage
import `fun`.kotlingang.kds.delegate.KDSDelegate
import `fun`.kotlingang.kds.delegate.property
import `fun`.kotlingang.kds.mutate.mutate
import `fun`.kotlingang.kds.mutate.mutateCommit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test
import kotlin.random.Random


val storage = KFileDataStorage(name = "data")

val randomDelegate = storage.property { Random.nextLong() }
var random by randomDelegate

var KFileDataStorage.random2 by KDSDelegate { Random.nextInt() }

var KFileDataStorage.launchesCount by KDSDelegate(serializer = Int.serializer()) { 0 }

var list by storage.property { mutableListOf<String>() }
val mutableList by storage.property { mutableListOf<String>() }


@DelicateCoroutinesApi
class StorageTests {
    @Test
    fun simpleStorageTest() = runBlocking {
        with(storage) {
            println("Awaiting loading")
            storage.loadDataBlocking()
            storage.loadData()
            println("Awaited loading")

            println("Launches count: ${++launchesCount}")
            println("Random value: $random2")

            println("ListBefore: $list")

            mutate {
                list += "Element"
            }

            println("List: $list")
            storage.commit()
        }
    }

    @Test
    fun mutableStorageTest() = runBlocking {
        with(storage) {
            mutateCommit {
                mutableList += "Test"
            }
        }
    }
}
