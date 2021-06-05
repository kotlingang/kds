package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.RawSetterGetter
import `fun`.kotlingang.kds.storage.StringDataStorage
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set


object KStringLocalDataStorage : StringDataStorage {
    @RawSetterGetter
    override fun putString(key: String, value: String) {
        localStorage[key] = value
    }
    @RawSetterGetter
    override fun getString(key: String) = localStorage[key]
}

