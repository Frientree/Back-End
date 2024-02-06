package com.d101.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.d101.presentation.R
import com.d101.presentation.main.MainActivity
import com.d101.presentation.music.BackgroundMusicService
import com.d101.presentation.music.BackgroundMusicService.Companion.MUSIC_NAME
import com.d101.presentation.music.BackgroundMusicService.Companion.PLAY
import com.d101.presentation.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
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
                    is SplashViewEvent.SetBackGroundMusic -> startMusicService(event.musicName)
                }
            }
        }
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
