import `fun`.kotlingang.deploy.DeployEntity

plugins {
    id(plugin.androidLibrary)
    kotlin(plugin.android)
}

configure<DeployEntity> {
    componentName = "release"
}

android {
    // Workaround since explicitApi() does not work for android
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"

    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        targetSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(core)
}
