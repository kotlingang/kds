[![Last Version](https://badge.kotlingang.fun/maven/fun/kotlingang/kds/core/)](https://maven.kotlingang.fun/fun/kotlingang/kds/kds)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/kotlingang/kds)

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serialization and delegates

## ⚠️ Current State
The library is not stable at the moment, that doesn't mean that it may crash, but does mean that any API may be changed. For now only contributors use this library in production (testing on real projects on Kotlin-JVM(2), Kotlin-JS(1), Kotlin-NODEJS(1) targets), if you want to use it more than playground, contact with us ([Alex](https://t.me/y9san9), [Neon](https://t.me/y9neon)) to let us know that we shouldn't make breaking changes and move the library into stable state. We also be glad if you share you vision of what the project structure and APIs should be. <br>
We really want someone to use our library, ask us about any questions and give suggestions.

## Example

### Storage
```kotlin
// Jvm / Nodejs
val storage = KFileDataStorage(name = "data.json")  // Created in ${workingDir}/data/name.json
val storage = KFileDataStorage(absolutePath = "...")
// Jvm
val storage = KFileDataStorage(file = [dataFile])
// Browser Js
val storage = KLocalDataStorage(key = "data")

var launchesCount by storage.property { 0 }
var list by storage.property { mutableListOf<String>() }


fun main() = storage.mutateBlocking { 
    list += "Element"
    launchesCount++
}
```

There are both blocking and asynchronous implementations (except JS-browser where there is only blocking implementation due to using `localStorage` instead of files).

Library may be fully customized since you can implement your own [DataManager](core/src/commonMain/kotlin/fun/kotlingang/kds/manager/DataManager.kt)

## Implementation
> When targeting JS, only IR is supported
> 
`$version` - library version, can be found in badge above <br>

All `kds` packages are located at repository [maven.kotlingang.fun](https://maven.kotlingang.fun/fun/kotlingang/kds) so make sure you include one.

### KFileDataStorage
KDataStorage async/sync [implementation](files) with files. <br>
**Platforms**: Jvm / NodeJS <br>
**Dependency**: `fun.kotlingang.kds:files:$version`

### KLocalDataStorage
KDataStorage sync [implementation](local-storage) with browser `localStorage` <br>
**Platforms**: Browser JS <br>
**Dependency**: `fun.kotlingang.kds:local-storage:$version`

### Custom
There **are** plans for other implementations (shared-preferences, ns-user-default, etc.), but if you want to create your own implementation, use following dependency

`// You can provide more platforms with PR/Issue` <br>
**Platforms**: Jvm / NodeJS / BrowserJS <br>
**Dependency**: `fun.kotlingang.kds:core:$version`

## Plans
Now the library primarily depends on JSON, it means that there is only ability to define custom `save(data)`, `load(): String` methods, but it would be nice to make more expressive `DataManager`, so I could integrate with Exposed (any kinds of Tables) and JSON could be just special case for KDS
