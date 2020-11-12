@file:Suppress("UNUSED_VARIABLE")


plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(coroutines)
}

kotlin {
    js(IR) {
        nodejs()
        browser()
    }
    sourceSets {
        val main by getting
        val test by getting
    }
}
