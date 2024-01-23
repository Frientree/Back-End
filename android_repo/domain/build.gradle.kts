plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    ktlint {
        version.set("0.48.0")
    }
}

dependencies {
    // Inject
    implementation("javax.inject:javax.inject:1")
}
