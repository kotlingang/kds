plugins {
    kotlin(plugin.js)
}

kotlin {
    explicitApi()

    js(IR) {
        browser()
        useCommonJs()
    }
}

dependencies {
    api(core)
}
