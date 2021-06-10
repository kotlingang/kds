package `fun`.kotlingang.kds

import `fun`.kotlingang.kds.annotation.InternalKDSApi
import android.app.Application


public object KDS {
    private var contextSource: Application? = null
    @InternalKDSApi
    public val context: Application get() = contextSource
        ?: error("Please ensure you called KDS.onCreate(this) in App.onCreate()")

    public fun onCreate(app: Application) {
        contextSource = app
    }
}
