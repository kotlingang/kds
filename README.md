![Last Version](https://img.shields.io/maven-metadata/v?label=gang&metadataUrl=https://maven.kotlingang.fun/com/kotlingang/kds/kds/maven-metadata.xml&logo=kotlin&logoColor=white)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/y9san9/kds)

# kds

Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serialization and delegates

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
