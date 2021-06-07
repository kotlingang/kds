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
        useCommonJs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(core)
                implementation(serialization)
            }
        }
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
