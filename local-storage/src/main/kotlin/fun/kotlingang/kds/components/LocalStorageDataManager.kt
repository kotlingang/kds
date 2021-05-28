package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.data_manager.BlockingContentDataManager
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set


class LocalStorageDataManager (
    private val key: String
) : BlockingContentDataManager {
    override fun loadDataBlocking() = localStorage[key] ?: "{}"

    override fun saveDataBlocking(text: String) {
        localStorage[key] = text
    }
}
