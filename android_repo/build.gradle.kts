// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("com.android.library") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN_VERSION apply false
    id("com.google.protobuf") version "0.9.4" apply false
    id("com.google.dagger.hilt.android") version Versions.HILT apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}
