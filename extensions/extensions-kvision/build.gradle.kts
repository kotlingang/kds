plugins {
    kotlin(plugin.js)
}

kotlin {
    explicitApi()

    js(IR) {
        browser()
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}

dependencies {
    api(core)
    implementation(kvision)
}
