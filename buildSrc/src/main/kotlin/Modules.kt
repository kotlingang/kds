import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


val KotlinDependencyHandler.core get() = project(":core")

val DependencyHandler.core get() = project(":core")
