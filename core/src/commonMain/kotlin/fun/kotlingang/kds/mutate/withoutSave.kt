package `fun`.kotlingang.kds.mutate

import `fun`.kotlingang.kds.KBlockingDataStorage
import `fun`.kotlingang.kds.annotation.DelicateKDSApi


/**
 * [mutate], [mutateBlocking], [mutateCommit] should be used instead
 */
@DelicateKDSApi
inline fun KBlockingDataStorage.withoutSave(crossinline block: () -> Unit) {
    autoSaveController.turnOff()
    block()
    autoSaveController.tryTurnOn()
}
