[![Last Version](https://badge.kotlingang.fun/maven/fun/kotlingang/kds/kds/)](https://maven.kotlingang.fun/fun/kotlingang/kds/kds)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/y9san9/kds)

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serialization and delegates

## ⚠️ Current State
The library is not stable at the moment, that doesn't mean that it may crash, but does mean that any API may be changed. For now only contributors use this library in production (testing on real projects on Kotlin-JVM(2), Kotlin-JS(1), Kotlin-NODEJS(1) targets), if you want to use it more than playground, contact with us ([Alex](https://t.me/y9san9), [Neon](https://t.me/y9neon)) to let us know that we shouldn't make breaking changes and move the library into stable state. We also be glad if you share you vision of what the project structure and APIs should be. <br>
We really want someone to use our library, ask us about any questions and give suggestions.

## Example

### Storage
```kotlin
val storage = KFileDataStorage(name = "data.json")
// Or
val storage = KFileDataStorage(absolutePath = "...")

var launchesCount by storage.property { 0 }
var list by storage.property { mutableListOf<String>() }


fun main() = storage.mutateBlocking { 
    list += "Element"
    launchesCount++
}
```

There are both blocking and asynchronous implementations (except JS-browser where there is only blocking implementation due to using `localStorage` instead of files).

Library may be fully customized since you can implement your own [DataManager](src/commonMain/kotlin/fun/kotlingang/kds/manager/DataManager.kt)

## Installation
`$version` - library version, can be found in badge above

### Groovy Gradle
```gradle
repositories {
    maven {
        url 'https://maven.kotlingang.fun/'
    }
}
dependencies {
    implementation "fun.kotlingang.kds:kds:$version"
}
```
### Kotlin Gradle Dsl
```gradle
repositories {
    maven("https://maven.kotlingang.fun/")
}
dependencies {
    implementation("fun.kotlingang.kds:kds:$version")
}
```
> Note: for nodejs you should use `kds-node` artifact
