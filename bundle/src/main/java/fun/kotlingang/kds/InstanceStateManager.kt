package `fun`.kotlingang.kds

import android.os.Bundle


public interface InstanceStateManager {
    public fun restoreInstanceState(bundle: Bundle?)
    public fun saveInstanceState(bundle: Bundle)
}
