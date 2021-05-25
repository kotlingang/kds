@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin(plugin.multiplatform)
    kotlin(plugin.serialization) version Version.SERIALIZATION_PLUGIN
}

group = AppInfo.PACKAGE
version = AppInfo.VERSION

repositories {
    mavenCentral()
    // Required for nodejs bindings. Waiting for deploy to mavenCentral()
    @kotlin.Suppress("DEPRECATION") jcenter()
}

kotlin {
    val jsType = Attribute.of("jsType", String::class.java)
    js(IR) {
        attributes.attribute(jsType, "browser")
        browser()
    }
    js("node", IR) {
        attributes.attribute(jsType, "node")
        useCommonJs()
        nodejs()
    }
    jvm()

    // native targets are not implemented; if you need it, create an issue
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(utils)
                implementation(coroutines)
                api(serialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val nodeMain by getting {
            dependencies {
                implementation(nodejsExternals)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nodeTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}

val root = project
allprojects {
    applyDeploy()
    group = root.group
    version = root.version
}
