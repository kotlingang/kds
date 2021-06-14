@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin(plugin.multiplatform)
}

kotlin {
    explicitApi()

    jvm()
    js(IR) {
        nodejs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(core)
                implementation(coroutines)
            }
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
