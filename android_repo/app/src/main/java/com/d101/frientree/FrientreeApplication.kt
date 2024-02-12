package com.d101.frientree

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FrientreeApplication : Application() {
    private var activityReferences = 0
    private var isActivityChangingConfigurations = false
    override fun onCreate() {
        super.onCreate()
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            BuildConfig.NAVER_LOGIN_CLIENT_NAME,
        )
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

            override fun onActivityStarted(p0: Activity) {
                if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                    LocalBroadcastManager.getInstance(this@FrientreeApplication).sendBroadcast(
                        Intent("FOREGROUND"),
                    )
                }
            }

            override fun onActivityStopped(activity: Activity) {
                isActivityChangingConfigurations = activity.isChangingConfigurations
                if (--activityReferences == 0 && !isActivityChangingConfigurations) {
                    LocalBroadcastManager.getInstance(this@FrientreeApplication).sendBroadcast(
                        Intent("BACKGROUND"),
                    )
                }
            }

            override fun onActivityResumed(p0: Activity) {}

            override fun onActivityPaused(p0: Activity) {}

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

            override fun onActivityDestroyed(p0: Activity) {}
        })
    }
}
