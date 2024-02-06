package com.d101.frientree

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.d101.presentation.BackgroundMusicPlayer
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FrientreeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            BuildConfig.NAVER_LOGIN_CLIENT_NAME,
        )
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
        BackgroundMusicPlayer.initMusicList(this)
        BackgroundMusicPlayer.playMusic(this, BackgroundMusicPlayer.getMusicList()[0])
    }

    private class AppLifecycleTracker : ActivityLifecycleCallbacks {
        private var activeActivities = 0

        override fun onActivityStarted(activity: Activity) {
            activeActivities++
            BackgroundMusicPlayer.resumeMusic()
        }

        override fun onActivityStopped(activity: Activity) {
            activeActivities--
            if (activeActivities == 0) {
                BackgroundMusicPlayer.pauseMusic()
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {
            if (activeActivities == 0) {
                BackgroundMusicPlayer.releaseMusicPlayer()
            }
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    }
}
