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

## Examples

### Files Storage

<details>
<summary>Expand</summary>
<p>

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

</p>
</details>

### Web Storage

<details>
<summary>Expand</summary>
<p>
    
```kotlin
object CookiesStorage : KLocalDataStorage() {
    val uniqueADId by property { Random.nextLong() }
}

fun main() {
    console.log("🙈 I'm tracking you, ${CookiesStorage.uniqueADId}!")
}
```
    
</p>
</details>

### SharedPreferences

<details>
<summary>Expand</summary>
<p>

```kotlin
// Initialize context first:
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KDS.onCreate(app = this)
    }
}
...
object SharedStorage : KSharedDataStorage() {
    var clicks by int { 0 }
}
...
import SharedStorage.clicks

class MainActivity : Activity() {
    override fun onCreate(bundle: Bundle?) {
        ...
        main.setOnClickListener {
            updateClicks(++clicks)
        }
    }
}
```

</p>
</details>
        
### Android Bundle State

<details>
<summary>Expand</summary>
<p>

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
> Custom `property` are also allowed there made with serialization to string followed by `bundle.putString`

</p>
</details>

### Mutate Example

<details>
<summary>Expand</summary>
<p>

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

</p>
</details>

### Mutate Entities

<details>
<summary>Expand</summary>
<p>

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

</p>
</details>

Note that the library is written in a way where you may **fully** customize it (add xml format for files/etc, implement java.Serializable support and so on, interfaces are common, so you may still use delegates, commits, mutations on it)

## Integrations
Library integrates with some other libraries providing convenient API for storing androidx `MutableState`. Integrations still require including storage dependency.

<details>
<summary>Expand</summary>

### Androidx MutableState
<details>
<summary>Expand</summary>

```kotlin
object ComposeStorage : ... {
    val username = mutableState<String>()
}
...
@Composable
fun UserNameText() {
    val username by remember { ComposeStorage.username }
    Text (
        text = username
    )
}
```

</details>

### KVision ObservableValue
<details>
<summary>Expand</summary>

```kotlin
object AppData : KLocalDataStorage() {
    val clicks = observableValue { 0 }
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

</details>

### Coroutines MutableStateFlow
<details>
<summary>Expand</summary>

```kotlin
object CoroutinesStorage : ... {
    // Use it everywhere you need to save state flow values
    val stateFlow by mutableStateFlow<String>()
}
```

</details>

</details>

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

## Integrations implementation

### Androidx Extensions
> MutableState [implementation](extensions/extensions-androidx/src/main/java/fun/kotlingang/kds/compose/mutable_state/StorageMutableState.kt)

**Platforms**: ![android][badge-android] <br>
**Dependency**: `fun.kotlingang.kds:extensions-androidx:$version`

## Custom
If you want to create your own implementation, take a look at the following dependencies

#### Core
> The core module with delegates and main interfaces

**Platforms**: Any <br>
**Dependency**: `fun.kotlingang.kds:core:$version`

#### Json
> The json module with abstraction over any storage uses json serialization (also proxies references to allow mutations)

**Platforms**: Any<br>
**Dependency**: `fun.kotlingang.kds:json:$version`

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
