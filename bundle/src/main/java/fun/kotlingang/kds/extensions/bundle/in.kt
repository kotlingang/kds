package `fun`.kotlingang.kds.extensions.bundle

import android.os.Bundle


internal operator fun Bundle.contains(key: String) = containsKey(key)
