package `fun`.kotlingang.kds.extensions.instance_state_manager

import `fun`.kotlingang.kds.InstanceStateManager
import android.os.Bundle


public inline fun InstanceStateManager.fillState(bundle: Bundle?, block: () -> Unit) {
    restoreInstanceState(bundle)
    block()
}
