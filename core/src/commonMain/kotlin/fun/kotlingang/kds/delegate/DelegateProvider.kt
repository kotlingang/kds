package `fun`.kotlingang.kds.delegate

import kotlin.reflect.KProperty


public fun interface DelegateProvider<in TReceiver, out TDelegate> {
    public operator fun provideDelegate(thisRef: TReceiver, property: KProperty<*>): TDelegate
}
