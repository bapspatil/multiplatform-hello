pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.library",
                "com.android.application" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
rootProject.name = "MultiplatformHello"

enableFeaturePreview("GRADLE_METADATA")

include(":shared")
include(":shared-mobile")
include(":android")
include(":server")
