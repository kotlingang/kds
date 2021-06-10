package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.InternalKDSApi
import android.app.Application


public object KDS {
    private var contextSource: Application? = null
    @InternalKDSApi
    public val context: Application = contextSource
        ?: error("Please ensure you called KDS.start() in App.onStart()")

    @OptIn(InternalKDSApi::class)
    public fun onCreate(app: Application) {
        contextSource = app
    }
}
