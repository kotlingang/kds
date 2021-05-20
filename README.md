# kds
[![Hits-of-Code](https://hitsofcode.com/github/y9san9/kds)](https://hitsofcode.com/view/github/y9san9/kds)


Kotlin Data Storage is a multiplatform coroutine-based kotlin library for saving Serializables in file with kotlinx.serlaization and delegates

## Example

### Storage
```kotlin
object Storage : KDataStorage() {  // or KDataStorage("name") or KDataStorage({ path("...") })
    var launchesCount by property(0)
    var list by property(mutableListOf<String>())
}


...
with(Storage) { 
    // Launches commit after changing mutable vars
    mutate {
        list.add("Element")
    }
    // Commit launched automatically because of delegate 
    launchesCount++
}
...
```

### Value Storage
```kotlin
val storage = KValueStorage(0)
val launchesCount by storage

...
println("${++launchesCount}")
...
```

### Edge cases
Because this framework is fully asynchronous, when using javascript, you should make `Storage.awaitLoading()` before usage. <br>

This also the reason why you should do `Storage.awaitLastCommit()` if your app doesn't run infinitely (like bots, android apps, etc). Any variable change leads to creating coroutine that writing file (it is implemented safely), and when you stop the app, you should await it.

So, the full example is:
```kotlin
suspend fun main() = with(Storage) {
    awaitLoading()
    foo = 0
    awaitLastCommit()
}
```
But in many apps it may be omitted. In non-js targets, you can omit `awaitLoading()`, in apps with any kind of lifecycle or that just infinitely running you can omit `awaitLastCommit()`.


#### More about load/save awaiting: [#1](https://github.com/y9san9/kds/issues/1)

## Installation
`$version` - library version, can be found in releases

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
