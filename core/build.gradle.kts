@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin(plugin.multiplatform)
}

kotlin {
    explicitApi()

    jvm()
    js(IR) {
        browser()
        nodejs()
        useCommonJs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(coroutines)
                implementation(serialization)
            }
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
