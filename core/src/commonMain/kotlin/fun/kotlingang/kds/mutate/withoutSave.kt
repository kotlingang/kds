package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KJsonBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


/**
 * [mutate], [mutateBlocking], [mutateCommit] should be used instead
 */
@DelicateKDSApi
inline fun KJsonBlockingDataStorage.withoutSave(crossinline block: () -> Unit) {
    autoSaveController.turnOff()
    block()
    autoSaveController.tryTurnOn()
}
