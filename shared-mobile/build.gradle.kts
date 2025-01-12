import org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("org.jetbrains.kotlin.xcode-compat") version "0.2.3"
}

val coroutineVersion = "1.3.0"
val ktorVersion = "1.2.4"

kotlin {
    android("android")
    xcode {
        setupFramework("ios") {
            baseName = "Shared"
            embedBitcode = BITCODE
            transitiveExport = true
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
            }
        }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":shared"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")

                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-json:$ktorVersion")
                api("io.ktor:ktor-client-serialization:$ktorVersion")
                api("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

                api("io.ktor:ktor-client-android:$ktorVersion")
                api("io.ktor:ktor-client-core-jvm:$ktorVersion")
                api("io.ktor:ktor-client-json-jvm:$ktorVersion")
                api("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                api("io.ktor:ktor-client-logging-jvm:$ktorVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))

                implementation("androidx.test:core:1.2.0")
                implementation("androidx.test.ext:junit:1.1.1")

                implementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")
            }
        }
        val iosMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutineVersion")

                api("io.ktor:ktor-client-ios:$ktorVersion")
                api("io.ktor:ktor-client-core-native:$ktorVersion")
                api("io.ktor:ktor-client-json-native:$ktorVersion")
                api("io.ktor:ktor-client-serialization-native:$ktorVersion")
                api("io.ktor:ktor-client-logging-native:$ktorVersion")
            }
        }
        val iosTest by getting {
            dependencies {
                implementation("io.ktor:ktor-client-mock-native:$ktorVersion")
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(15)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// TODO For no reason I can explain, binaries.getTest() isn't resolving here despite this being identical to :shared module
//tasks.create("iosTest") {
//    dependsOn("linkDebugTestIos")
//    doLast {
//        val testBinaryPath =
//            (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getTest("DEBUG").outputFile.absolutePath
//        exec {
//            commandLine("xcrun", "simctl", "spawn", "iPhone Xʀ", testBinaryPath)
//        }
//    }
//}
//tasks["check"].dependsOn("iosTest")
