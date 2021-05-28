plugins {
    kotlin(plugin.js)
}

kotlin {
    js(BOTH) {
        browser()
        useCommonJs()
    }
}

dependencies {
    api(core)
    implementation(serialization)
}
