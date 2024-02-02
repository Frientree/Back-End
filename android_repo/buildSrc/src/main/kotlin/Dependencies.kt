object Versions {
    const val ANDROID_GRADLE_PLUGIN = "8.1.4"
    const val KOTLIN_VERSION = "1.9.0"
    const val APP_COMPAT = "1.6.1"
    const val MATERIAL = "1.11.0"
    const val JUNIT = "4.13.2"
    const val ANDROIDX_JUNIT = "1.1.5"
    const val ESPRESSO_CORE = "3.5.1"
    const val HILT = "2.48"
    const val INJECT = "1"
    const val RETROFIT = "2.9.0"
    const val OKHTTP = "4.10.0"
    const val NAV_FRAGMENT = "2.7.6"
    const val NAV_UI = "2.7.6"
    const val VIEWMODEL = "2.7.0"
    const val FRAGMENT = "1.6.2"
    const val ROOM_DB = "2.5.0"
    const val LOTTIE = "6.3.0"
    const val DATA_STORE = "1.0.0"
    const val PROTO_BUF = "3.25.1"
    const val COROUTINES_CORE = "1.7.1"
    const val GLIDE = "4.16.0"
}

object Libraries {
    const val coreKtx = "androidx.core:core-ktx:${Versions.KOTLIN_VERSION}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val junit = "junit:junit:${Versions.JUNIT}"
    const val androidxJunit = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val inject = "javax.inject:javax.inject:${Versions.INJECT}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAV_FRAGMENT}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.NAV_UI}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.VIEWMODEL}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.ROOM_DB}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM_DB}"
    const val lottie = "com.airbnb.android:lottie:${Versions.LOTTIE}"
    const val dataStore = "androidx.datastore:datastore:${Versions.DATA_STORE}"
    const val dataStorePrefs = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE}"
    const val dataStoreCore = "androidx.datastore:datastore-core:${Versions.DATA_STORE}"
    const val protoBuf = "com.google.protobuf:protobuf-javalite:${Versions.PROTO_BUF}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES_CORE}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
}
