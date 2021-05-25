@file:Suppress("UNUSED_VARIABLE")


plugins {
    kotlin(plugin.multiplatform)
}

repositories {
    mavenCentral()
}
kotlin {
    js(IR) {
        useCommonJs()
        browser()
        nodejs()
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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
