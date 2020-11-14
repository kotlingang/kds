package com.kotlingang.kds

import com.kotlingang.kds.utils.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlin.test.Test


object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(listOf<String>())
}

object Storage2 : KDataStorage("storage2") {
    var launchesCount by property(0)
}


class StorageTests {
    @Test
    fun simpleStorageTest() = GlobalScope.runBlocking {
        with(Storage) {
            awaitLoading()

            println("Launches: ${++launchesCount}")

            val myList = list.toMutableList()
            myList.add("Element")
            list = myList

            println("List: $myList")

            awaitSaving()
        }
    }
    @Test
    fun storageTestWithoutLoadAwaiting() = GlobalScope.runBlocking {
        with(Storage2) {
            println("Launches: ${++launchesCount}")
            awaitSaving()
        }
    }
}
