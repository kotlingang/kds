@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin(plugin.multiplatform)
}

kotlin {
    jvm()
    js(BOTH) {
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
