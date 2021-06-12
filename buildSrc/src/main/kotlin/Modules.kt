@file:Suppress("ObjectPropertyName")

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


val KotlinDependencyHandler.core get() = project(":core")
val KotlinDependencyHandler.json get() = project(":json")
val KotlinDependencyHandler.files get() = project(":files")

val DependencyHandler.`local-storage` get() = project(":local-storage")
val DependencyHandler.json get() = project(":json")
val DependencyHandler.core get() = project(":core")
val DependencyHandler.`shared-preferences` get() = project(":shared-preferences")
val DependencyHandler.`android-app-provider` get() = project(":android-app-provider")
val DependencyHandler.bundle get() = project(":bundle")
