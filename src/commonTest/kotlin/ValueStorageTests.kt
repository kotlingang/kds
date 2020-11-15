package com.kotlingang.kds


import com.kotlingang.kds.wrapper.KValueStorage
import kotlinx.coroutines.GlobalScope
import kotlin.test.Test


val valueStorage = KValueStorage(0)
var launchNumber by valueStorage

class ValueStorageTests {
    @Test
    fun simpleValueStorageTest() = GlobalScope.runTestBlocking {
        valueStorage.awaitLoading()
        println(++launchNumber)
        valueStorage.awaitLastCommit()
    }
}
