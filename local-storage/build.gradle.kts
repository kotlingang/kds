plugins {
    kotlin(plugin.js)
}

kotlin {
    js(IR) {
        browser()
        useCommonJs()
    }
}

dependencies {
    api(core)
}
