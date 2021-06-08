allprojects {
    repositories {
        mavenCentral()
        google()
        // Required for nodejs bindings. Waiting for deploy to mavenCentral()
        @kotlin.Suppress("DEPRECATION") jcenter()
    }
}

subprojects {
    group = AppInfo.PACKAGE
    version = AppInfo.VERSION
    applyDeploy()
}
