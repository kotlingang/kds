package com.kotlingang.kds

import com.kotlingang.kds.utils.runTestBlocking
import com.kotlingang.kds.wrapper.KValueStorage
import kotlin.test.Test


val valueStorage = KValueStorage(0)
var launchNumber by valueStorage

class ValueStorageTests {
    @Test
    fun simpleValueStorageTest() = runTestBlocking {
        valueStorage.awaitLoading()
        println(++launchNumber)
        valueStorage.awaitSaving()
    }
}
