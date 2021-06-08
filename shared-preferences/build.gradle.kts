import `fun`.kotlingang.deploy.DeployEntity

plugins {
    id(plugin.androidLibrary)
    kotlin(plugin.android)
}

android {
    // Workaround since explicitApi() does not work for android
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"

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
    api(core)
}
