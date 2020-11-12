package com.kotlingang.kds

import com.kotlingang.kds.utils.runTestBlocking
import kotlin.test.Test


object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(listOf<String>())
}


class StorageTests {
    @Test
    fun simpleStorageTest() = runTestBlocking {
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
}