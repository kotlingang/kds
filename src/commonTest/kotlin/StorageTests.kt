package com.kotlingang.kds

import kotlinx.coroutines.GlobalScope
import kotlin.test.Test


object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(listOf<String>())
    val mutableList by property(mutableListOf<String>())
}


class StorageTests {
    @Test
    fun simpleStorageTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            awaitLoading()

            println("Launches: ${++launchesCount}")

            val myList = list.toMutableList()
            myList.add("Element")
            list = myList

            println("List: $myList")

            awaitLastCommit()
        }
    }
    @Test
    fun storageTestWithoutLoadAwaiting() = GlobalScope.runTestBlocking {
        with(Storage) {
            awaitLoading()  // for JS
            println("Launches: ${++launchesCount}")
            awaitLastCommit()
        }
    }
    @Test
    fun mutableStorageTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            awaitLoading()  // for JS
            mutableList += "Test"
            commit()
        }
    }
}
