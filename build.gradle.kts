@file:Suppress("UNUSED_VARIABLE")
import `fun`.kotlingang.deploy.DeployEntity

plugins {
    kotlin(plugin.multiplatformn)
    kotlin(plugin.serialization) version Version.SERIALIZATION_PLUGIN
}

group = AppInfo.PACKAGE
version = AppInfo.VERSION

repositories {
    mavenCentral()
}
kotlin {
    val jsType = Attribute.of("jsType", String::class.java)
    js("browser") {
        attributes.attribute(jsType, "browser")
        browser()
    }
    js("node") {
        attributes.attribute(jsType, "node")
        useCommonJs()
        nodejs()
    }

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
    group = AppInfo.PACKAGE
    artifactId = AppInfo.ARTIFACT_ID
    version = AppInfo.VERSION
    name = AppInfo.NAME
    description = AppInfo.DESCRIPTION
}

/**
 * Enables deploy for `:` if `deploy.properties` exists.
 */
project.applyDeploy()