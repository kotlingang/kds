package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.sync.platformSynchronized


/**
 * Used to achieve thread safety for [autoSave]
 */
class AutoSaveController internal constructor() {
    private var turnOffRequests = 0

    val autoSave get() = turnOffRequests == 0

    fun turnOff() = platformSynchronized(lock = this) { turnOffRequests++ }
    fun turnOn() { turnOffRequests = 0 }
    fun tryTurnOn() = platformSynchronized(lock = this) { turnOffRequests-- }
}
