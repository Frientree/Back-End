plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

android {
    namespace = "com.d101.data"
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":domain"))

    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidxJunit)
    androidTestImplementation(Libraries.espressoCore)

    // Hilt
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)
}
