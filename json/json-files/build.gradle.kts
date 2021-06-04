plugins {
    kotlin(plugin.multiplatform)
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
        useCommonJs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(json)
                implementation(files)
                implementation(serialization)
                implementation(coroutines)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}