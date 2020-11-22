package com.kotlingang.kds

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.test.Test


@Serializable
class MassiveTestClass

object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var random by property { Random.nextLong() }

    val random2Delegate = property { Random.nextInt() }
    var random2 by random2Delegate

    var launchesCount by property(0)
    var list by property(listOf<String>())
    val mutableList by property(mutableListOf<String>())
    var massiveTestProp by property {
        MassiveTestClass()
    }
}


class StorageTests {
    @Test
    fun simpleStorageTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            println("Awaiting loading")
            awaitLoading()
            println("Awaited loading")

            println("Launches count: ${++launchesCount}")
            println("Random value: $random")

            val myList = list.toMutableList()
            println("My list set")
            myList.add("Element")
            list = myList
            println("My list saved")

            println("List: $myList")

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
    @Test
    fun clearTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            println(random2)
            random2Delegate.clear()
            println(random2)
        }
    }
}
