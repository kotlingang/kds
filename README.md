# kds
[![Download](https://img.shields.io/bintray/v/y9san9/kotlingang/kds)](https://bintray.com/y9san9/kotlingang/kds/_latestVersion)
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/y9san9/kds)


Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serlaization and delegates

## Example

### Storage
```kotlin
object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(mutableListOf<String>())
}


suspend fun main() = with(Storage) {
    awaitLoading()  // The loading is async and you should wait for it

    println("Launches: ${++launchesCount}")
    
    list.add("Element")
    // should be commit to save edited list
    launchCommit()

    println("List: $myList")

    awaitLastCommit()  // If there is an launched coroutine with storage saving, await it before closing program
}
```

### Value Storage
```kotlin
val storage = KValueStorage(0)
val launchesCount by storage

suspend fun main() {
    storage.awaitLoading()
    println("${++launchesCount}")
    storage.awaitLastCommit()
}
```

#### More about load/save awaiting: [#1](https://github.com/y9san9/kds/issues/1)

## Installaton
`$version` - library version, can be found in bintray badge

### Groovy Gradle
```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/y9san9/kotlingang'
    }
}
dependencies {
    implementation "com.kotlingang.kds:kds:$version"
}
```
### Kotlin Gradle Dsl
```gradle
repositories {
    maven("https://dl.bintray.com/y9san9/kotlingang")
}
dependencies {
    implementation("com.kotlingang.kds:kds:$version")
}
```
