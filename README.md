![Last Version](https://img.shields.io/maven-metadata/v?label=gang&metadataUrl=https://maven.kotlingang.fun/com/kotlingang/kds/kds/maven-metadata.xml&logo=kotlin&logoColor=white)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/y9san9/kds)

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serialization and delegates

## ⚠️ Current State
The library is not stable at the moment, that doesn't mean that it may crash, but mean that any API may be changed. For now only contributors use this library in production (testing on real projects on Kotlin-JVM(2), Kotlin-JS(1), Kotlin-NODEJS(1) targets), if you want to use it more than playground, contact with us ([Alex](https://t.me/y9san9), [Neon](https://t.me/y9neon) to let us know that we shouldn't make breaking changes and move the library into stable state. We also be glad if you share you vision of what the project structure and APIs should be. <br>
We really want someone to use our library, ask us about any questions and give suggestions.

## Example

### Storage
```kotlin
object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(mutableListOf<String>())
}


suspend fun main() = Storage.commitMutate { 
    list += "Element"
    launchesCount++
}
```

### Value Storage
```kotlin
val storage = KValueStorage(0)
val launchesCount by storage

suspend fun main() = storage.commitMutate {
    println("${++launchesCount}")
}
```

## Non-coroutines way
There is also a way for changing the storage without suspend context in infinitely-running apps:
```kotlin
class KindaActivity : CoroutineScope by ... {
    // Scope is still optional
    object Storage : KDataStorage(scope = this) {  // or KDataStorage("name") or KDataStorage({ path("...") })
        var launchesCount by property(0)
        var list by property(mutableListOf<String>())
    }
    
    fun onCreate() = with(Storage) {
        // Commit launches automatically since it is calling delegate
        launchesCount++
        
        // Since list is mutable object, explicit mutation declaration required
        mutate {
            list += "Element"
        }
    }
}
```

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
