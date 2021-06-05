[![Last Version](https://badge.kotlingang.fun/maven/fun/kotlingang/kds/core/)](https://maven.kotlingang.fun/fun/kotlingang/kds/kds)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/kotlingang/kds)

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serialization and delegates.

## Use case
If you need to store any kind of preferences in your app, you would probably use this framework since it has common API for any platform you need.

## ⚠️ Current State
The library is not stable at the moment, that doesn't mean that it may crash, but does mean that any API may be changed. For now only contributors use this library in production (testing on real projects on Kotlin-JVM(2), Kotlin-JS(1), Kotlin-NODEJS(1) targets), if you want to use it more than playground, contact with us ([Alex](https://t.me/y9san9), [Neon](https://t.me/y9neon)) to let us know that we shouldn't make breaking changes and move the library into stable state. We also be glad if you share you vision of what the project structure and APIs should be. <br>
We really want someone to use our library, ask us about any questions and give suggestions.

**Stability**: _PROTOTYPE_

## Example

### Files Storage
```kotlin
object ProgramData : KFileDataStorage() {
    val userName by property<String>()
}

fun main() {
    if(ProgramData.userName == null) {
        println("Hi dear user, how should I call you?")
        userName = readLine() ?: "Anonymous"
        println("Okay $userName, see you")
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

### Mutate Example
There is also an API to use mutable objects
```kotlin
data class Item (
    ...
)

object MainStorage : ... {
    val items by property { mutableListOf<Item>() }
}

// Launches asynchronous commit after block()
fun addItem() = MainStorage.mutate {
    items += Item(...)
}
// Suspends until commit
suspend fun addItem() = MainStorage.mutateCommit {
    items += Item(...)
}
// Blocking mutation
fun addItem() = MainStorage.mutateBlocking {
    items += Item(...)
}

suspend fun main() {
    // Launches commit and cancels previous one
    MainStorage.launchCommit()
    // Suspends until commit
    MainStorage.commit()
    // Blocking commit
    MainStorage.commitBlocking()
}
```

There are both blocking and asynchronous implementations (except JS-browser where there is only blocking implementation due to using `localStorage` instead of files).

Note that the library written in a way where you may **fully** customize it (add xml format for files/etc, implement java.Serializable support and so on, interfaces are common, so you may still use delegates, commits, mutations on it)

## Implementation
> When targeting JS, only IR is supported

`$version` - library version, can be found in badge above <br>

All `kds` packages located at repository [maven.kotlingang.fun](https://maven.kotlingang.fun/fun/kotlingang/kds), so make sure you include one.

### KFileDataStorage
> KDataStorage async/sync [implementation](json/json-files) with files.

**Platforms**: Jvm / NodeJS <br>
**Dependency**: `fun.kotlingang.kds:json-files:$version`

### KLocalDataStorage
> KDataStorage sync [implementation](json/json-local-storage) with browser `localStorage`

**Platforms**: Browser JS <br>
**Dependency**: `fun.kotlingang.kds:local-storage:$version`

### Custom
There **are** plans for other implementations (bundle, shared-preferences, ns-user-default, etc.), but if you want to create your own implementation, take a look at following dependencies

#### Core
> The core module with delegates and main interfaces

**Platforms**: Any <br>
**Dependency**: `fun.kotlingang.kds:core:$version`

#### Json
> The json module with abstraction over any storage uses json serialization (also proxies references to allow mutations)

**Platforms**: Any<br>
**Dependency**: `fun.kotlingang.kds:json:$version`

## Plans
There are a lot of possibilities to customize library, main goal for now is stabilization of user API.

**Ideas**: <br>
I think it may be cool to integrate library with [kvision](https://github.com/rjaros/kvision), `compose`, etc.
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
I would separate `json` module (add `refs-proxy` module to proxy references) and `files` (add `content-storage` module to add abstraction over storages that converting data to Map<String, String> and then serializing it to content)
