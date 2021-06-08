package `fun`.kotlingang.kds

import android.app.Application


public object KDS {
    private var contextSource: Application? = null
    internal val context: Application = contextSource ?: error("Please ensure you called KDS.start() in App.onStart()")

    public fun start(context: Application) {
        contextSource = context
    }
}
