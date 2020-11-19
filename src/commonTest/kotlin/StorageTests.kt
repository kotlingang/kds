package com.kotlingang.kds

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.test.Test


@Serializable
class MassiveTestClass

object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var random by property { Random.nextLong() }
    var launchesCount by property(0)
    var list by property(listOf<String>())
    val mutableList by property(mutableListOf<String>())
    var massiveTestProp by property {
        println("Created")
        MassiveTestClass()
    }
}


class StorageTests {
    @Test
    fun simpleStorageTest() = GlobalScope.runTestBlocking {
        with(Storage) {
            awaitLoading()

            println("Launches: ${++launchesCount}")
            println("Random value: $random")

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
    @Test
    fun massiveTest() = GlobalScope.runTestBlocking {
        var valueRef: Any? = null
        with(Storage) {
            for(i in 1..1000) launch {
                val value = massiveTestProp
                valueRef = valueRef ?: value
                println(valueRef === value)
            }
        }
    }
}
