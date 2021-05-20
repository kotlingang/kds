@file:Suppress("UNUSED_VARIABLE")


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.10"
    `maven-publish`
}

group = Library.GROUP
version = Library.VERSION

repositories {
    jcenter()
    mavenCentral()
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    val jsType = Attribute.of("jsType", String::class.java)
    js("browser", IR) {
        attributes.attribute(jsType, "browser")
        browser()
    }
    js("node", IR) {
        attributes.attribute(jsType, "node")
        useCommonJs()
        nodejs()
    }
    // node target is not implemented; if you need it, create an issue
    // native targets are not implemented; if you need it, create an issue
    // ...
    
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
        val jvmMain by getting {
            dependencies {

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val browserMain by getting {
            dependencies {
            }
        }
        val browserTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nodeMain by getting {
            dependencies {
                implementation(nodejsExternals)
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
    group = root.group
    version = root.version
}

project.configure<DeployEntity> {
    group = Library.GROUP
    artifactId = Library.ARTIFACT_ID
    version = Library.VERSION
    name = Library.NAME
    description = Library.DESCRIPTION
}

/**
 * Enables deploy for `:` if `deploy.properties` exists.
 */
project.applyDeploy()