package `fun`.kotlingang.kds.manager

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set


class LocalStorageDataManager (
    private val key: String
) : BlockingDataManager {
    override fun loadDataBlocking() = localStorage[key] ?: "{}"

    override fun saveDataBlocking(text: String) {
        localStorage[key] = text
    }
}
