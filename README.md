[![Last Version](https://badge.kotlingang.fun/maven/fun/kotlingang/kds/core/)](https://maven.kotlingang.fun/fun/kotlingang/kds/)
[![Hits-of-Code](https://hitsofcode.com/github/kotlingang/kds)](https://hitsofcode.com/view/github/kotlingang/kds) <br>

![badge][badge-nodejs]
![badge][badge-js]
![badge][badge-jvm]
![badge][badge-android]

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for storing Serializables with kotlinx.serialization and delegates.

## Use case
If you need to store any kind of preferences in your app, you would probably use this framework since it has a common API for any platform you need.

## ⚠️ Current State
We are experimental, which means API breaking changes may be performed in minor releases.

## Example

### Files Storage
```kotlin
import ProgramData.userName

object ProgramData : KFileDataStorage() {
    val userName by property<String>()  // shortcut for property<String?> { null }
}

fun main() {
    if(userName == null) {
        println("Hi dear user, how should I call you?")
        userName = readLine() ?: "Anonymous"
        println("Okay ${userName}, see you")
    } else {
        println("Glad to see you again, $userName")
    }
}
```

### Web Storage
```kotlin
object CookiesStorage : KLocalDataStorage() {
    val uniqueADId by property { Random.nextLong() }
}

fun main() {
    console.log("🙈 I'm tracking you, ${CookiesStorage.uniqueADId}!")
}
```

### Android Bundle State
You can also store android app state with the library
```kotlin
class MainActivity : Activity() {
    private val state = object : KBundleDataStorage() {
        var score by int { 0 }  // This will be automatically saved and restored
    }
    
    override fun onCreate(bundle: Bundle?) = state.fillState(bundle) {
        super.onCreate(bundle)
    }
    // OR
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        state.restoreInstanceState(bundle)
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.saveInstanceState(outState)
    }
}
```
`property` are also allowed there with serialization to string followed by `bundle.putString`


### Mutate Example
There is also an API to use mutable objects
```kotlin
data class Item (
    var foo: Foo? = null
)

object MainStorage : ... {
    val item by property(::Item)
}

// Launches an asynchronous commit after block()
fun editItem() = MainStorage.mutate {
    item.foo = ...
}
// Suspends until commit
suspend fun editItem() = MainStorage.mutateCommit {
    item.foo = ...
}
// Blocking mutation
fun editItem() = MainStorage.mutateBlocking {
    item.foo = ...
}

suspend fun main() {
    // Launches a commit and cancels the previous one
    MainStorage.launchCommit()
    // Suspends until commit
    MainStorage.commit()
    // Blocking commit
    MainStorage.commitBlocking()
}
```

### Mutate Entities
There are some (experimental for now) entities which may automatically perform save operation on mutate:
```kotlin
object MainStorage : ... {
    val list by storageList<Boolean>()
    val map by storageMap<String, Int>()
    val set by storageSet { mutableSetOf(1, 2, 3) }
}

fun main() {
    // Then any mutation on this entities will perform save
    // The saving operation will same as operation when assigning variable to new value
    // It means that in KFileDataStorage async save will be invoked, while in KLocalDataStorage blocking `put` method
    MainStorage.list += true
}
```

There are both blocking and asynchronous implementations (except JS-browser where there is only blocking implementation due to using `localStorage` instead of files).

Note that the library is written in a way where you may **fully** customize it (add xml format for files/etc, implement java.Serializable support and so on, interfaces are common, so you may still use delegates, commits, mutations on it)

## Implementation
> When targeting JS, only IR is supported

`$version` - the library version, can be found in badge above <br>

All `kds` packages are located at repository [maven.kotlingang.fun](https://maven.kotlingang.fun/fun/kotlingang/kds), so make sure you include one.

### KFileDataStorage
> KDataStorage async/sync [implementation](json/json-files) with files.

**Platforms**: ![jvm][badge-jvm] ![nodejs][badge-nodejs] <br>
**Dependency**: `fun.kotlingang.kds:json-files:$version`

### KLocalDataStorage
> KDataStorage sync [implementation](json/json-local-storage) with browser `localStorage`

**Platforms**: ![js][badge-js] <br>
**Dependency**: `fun.kotlingang.kds:json-local-storage:$version`

### KSharedDataStorage
> KDataStorage async [implementation](json/json-shared-preferences) with android `SharedPreferences`

**Platforms**: ![android][badge-android] <br>
**Dependency**: `fun.kotlingang.kds:json-shared-preferences:$version` <br>
**Example**: GitHub [repo](https://github.com/kotlingang/kds-android-example)

### KBundleDataStorage
> KDataStorage sync [implementation](json/json-bundle) with android `Bundle`

**Platforms**: ![android][badge-android] <br>
**Dependency**: `fun.kotlingang.kds:json-bundle:$version` <br>
**Example**: GitHub [repo](https://github.com/kotlingang/kds-android-example)

### Custom
There **are** plans for other implementations (bundle, ns-user-default, etc.), but if you want to create your implementation, take a look at the following dependencies

#### Core
> The core module with delegates and main interfaces

**Platforms**: Any <br>
**Dependency**: `fun.kotlingang.kds:core:$version`

#### Json
> The json module with abstraction over any storage uses json serialization (also proxies references to allow mutations)

**Platforms**: Any<br>
**Dependency**: `fun.kotlingang.kds:json:$version`

## Plans
There are a lot of possibilities to customize the library, the main goal, for now, is a stabilization of user API.

**Ideas**: <br>
I think it may be cool to integrate the library with [kvision](https://github.com/rjaros/kvision), `compose`, etc.
### KVision integration example

```kotlin
object AppData : KLocalDataStorage() {
    val clicks by kvisionState<Int>()
}

class App : Application() {
    override fun start() {
        root(id = "root") {
            vPanel {
                h1(AppData.clicks) { clicks ->
                    + "Clicked $clicks times"
                }
                button(text = "Click!") {
                    onClick {
                        // Changes still handled by storage
                        clicks.value++ 
                    }
                }
            }
        }
    }
}

fun main() = startApplication(::App)
```

**Near future**: <br>
I would separate the `json` module (add `refs-proxy` module to proxy references) and `files` (add `content-storage` module to add abstraction over storages that converting data to Map<String, String> and then serializing it to content)

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat
[badge-nodejs]: http://img.shields.io/badge/platform-nodejs-68a063.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat
[badge-linux]: http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat
[badge-windows]: http://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/platform-macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/platform-tvos-808080.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/platform-wasm-624FE8.svg?style=flat
