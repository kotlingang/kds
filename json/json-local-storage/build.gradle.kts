plugins {
    kotlin(plugin.js)
}

kotlin {
    explicitApi()

    js(IR) {
        browser()
        useCommonJs()
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}

dependencies {
    api(json)
    implementation(`local-storage`)
}
