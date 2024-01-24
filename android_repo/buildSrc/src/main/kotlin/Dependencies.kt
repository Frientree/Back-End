object Versions {
    const val ANDROID_GRADLE_PLUGIN = "8.1.4"
    const val KOTLIN_VERSION = "1.9.0"
    const val APP_COMPAT = "1.6.1"
    const val MATERIAL = "1.11.0"
    const val JUNIT = "4.13.2"
    const val ANDROIDX_JUNIT = "1.1.5"
    const val ESPRESSO_CORE = "3.5.1"
    const val HILT = "2.44"
    const val INJECT = "1"
}

object Libraries{
    const val coreKtx = "androidx.core:core-ktx:${Versions.KOTLIN_VERSION}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val junit = "junit:junit:${Versions.JUNIT}"
    const val androidxJunit = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val inject = "javax.inject:javax.inject:${Versions.INJECT}"
}
