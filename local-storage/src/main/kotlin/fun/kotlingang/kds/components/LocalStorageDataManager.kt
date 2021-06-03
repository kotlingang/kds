package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.manager.ContentDataManager
import `fun`.kotlingang.kds.manager.PropertyDataManager
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set


object LocalStorageDataManager : PropertyDataManager {
    override fun put(key: String, text: String) {
        localStorage[key] = text
    }

    override fun get(key: String)= localStorage[key]
}
