import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1"

const val nodejsExternals = "org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7"


val KotlinDependencyHandler.jsCommon get() = project(":jsCommon")
