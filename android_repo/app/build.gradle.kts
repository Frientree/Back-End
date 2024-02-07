import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("com.google.gms.google-services")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.d101.frientree"
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        applicationId = "com.d101.frientree"
        minSdk = AppConfig.MIN_SDK
        targetSdk = AppConfig.TARGET_SDK
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "BASE_URL",
            gradleLocalProperties(rootDir).getProperty("BASE_URL"),
        )

        buildConfigField(
            "String",
            "NAVER_LOGIN_CLIENT_ID",
            "\"${properties["NAVER_LOGIN_CLIENT_ID"]}\"",
        )
        buildConfigField(
            "String",
            "NAVER_LOGIN_CLIENT_SECRET",
            "\"${properties["NAVER_LOGIN_CLIENT_SECRET"]}\"",
        )

        buildConfigField(
            "String",
            "NAVER_LOGIN_CLIENT_NAME",
            "\"${properties["NAVER_LOGIN_CLIENT_NAME"]}\"",
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    ktlint {
        version.set("0.48.0")
    }
}

dependencies {

    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxJunit)
    androidTestImplementation(Libraries.espressoCore)

    // Hilt
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)

    // Retrofit
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGson)

    // Okhttp
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogging)

    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)

    // DataStore
    implementation(Libraries.dataStore)
    implementation(Libraries.dataStoreCore)
    implementation(Libraries.protoBuf)

    implementation(Libraries.naverOAuth)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")
}
kapt {
    correctErrorTypes = true
}
