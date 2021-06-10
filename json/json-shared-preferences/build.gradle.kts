import `fun`.kotlingang.deploy.DeployEntity

plugins {
    id(plugin.androidLibrary)
    kotlin(plugin.android)
}

android {
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"

    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        targetSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
    }

    buildTypes {
        val release by getting {

        }
        val debug by getting {

        }
    }
}

configure<DeployEntity> {
    componentName = "release"
}

dependencies {
    api(json)
    implementation(`shared-preferences`)
}
