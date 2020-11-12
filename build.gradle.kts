@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.konan.properties.loadProperties


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.10"
    `maven-publish`
}

group = "com.kotlingang.kds"
version = "1.0"

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
    js("browser", IR) {
        browser()
    }
    js("node", IR) {
        useCommonJs()
        nodejs()
    }
    // node target is not implemented; if you need it, create an issue
    // native targets are not implemented; if you need it, create an issue
    // ...
    
    sourceSets {
        val commonMain by getting {
            dependencies {
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
                implementation(jsCommon)
            }
        }
        val browserTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nodeMain by getting {
            dependencies {
                implementation(jsCommon)
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


val properties = loadProperties(project.rootProject.file("local.properties").absolutePath)

val bintrayUser: String? by properties
val bintrayApiKey: String? by properties

val projectName = project.name

allprojects {
    apply(plugin = "maven-publish")
    publishing {
        val vcs = "https://github.com/kotlingang/sketchcode-api-lib"

        publications.filterIsInstance<MavenPublication>().forEach { publication ->
            publication.pom {
                name.set(project.name)
                description.set(project.description)
                url.set(vcs)

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("y9san9")
                        name.set("Alex Sokol")
                        organization.set("Kotlingang")
//                        organizationUrl.set("https://sketchcode.fun")
                    }

                }
                scm {
                    url.set(vcs)
                    tag.set(project.version.toString())
                }
            }
        }

        if (bintrayUser != null && bintrayApiKey != null) {
            repositories {
                maven {
                    name = "bintray"
                    url = uri("https://api.bintray.com/maven/y9san9/kotlingang/$projectName/;publish=1;override=1")
                    credentials {
                        username = bintrayUser
                        password = bintrayApiKey
                    }
                }
            }
        }
    }
}
