package com.d101.presentation.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.d101.presentation.BuildConfig
import com.d101.presentation.R
import com.d101.presentation.main.MainActivity
import com.d101.presentation.music.BackgroundMusicService
import com.d101.presentation.music.BackgroundMusicService.Companion.MUSIC_NAME
import com.d101.presentation.music.BackgroundMusicService.Companion.PLAY
import com.d101.presentation.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        collectEvent()
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    SplashViewEvent.ShowSplash -> viewModel.showSplash()
                    SplashViewEvent.AutoSignInFailure -> navigateToSignIn()
                    SplashViewEvent.AutoSignInSuccess -> navigateToMain()
                    is SplashViewEvent.CheckAppStatus -> checkAppStatus(event)
                    SplashViewEvent.OnFailCheckAppStatus -> showToast("App Status check failed")
                    is SplashViewEvent.SetBackGroundMusic -> startMusicService(event.musicName)
                }
            }
        }
    }

    private fun checkAppStatus(event: SplashViewEvent.CheckAppStatus) {
        if (!event.appAvailable) {
            showToast("App is not available")
            ActivityCompat.finishAffinity(this)
            return
        }
        if (needToUpdate(event.minVersion)) {
            showToast("최신 버전으로 업데이트가 필요합니다.")
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.storeUrl))
                startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://play.google.com/store/apps/details?id=$packageName",
                    ),
                )
                startActivity(intent)
            }
            ActivityCompat.finishAffinity(this)
            return
        }

        viewModel.checkSignInStatus()
    }

    private fun needToUpdate(minVersion: String): Boolean {
        val appVersionArr = BuildConfig.APP_VERSION_NAME.split(".")
        val appMajor = appVersionArr[0].toInt()
        val appMinor = appVersionArr[1].toInt()
        val appPatch = appVersionArr[2].toInt()

        val minVersionArr = minVersion.split(".")
        val minMajor = minVersionArr[0].toInt()
        val minMinor = minVersionArr[1].toInt()
        val minPatch = minVersionArr[2].toInt()

        if (appMajor < minMajor) return true
        if (appMinor < minMinor) return true
        if (appPatch < minPatch) return true
        return false
    }

    private fun showToast(message: String) {
        CustomToast.createAndShow(this, message)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMusicService(musicName: String) {
        startService(
            Intent(this, BackgroundMusicService::class.java).apply {
                action = PLAY
                putExtra(MUSIC_NAME, musicName)
            },
        )
    }
}
