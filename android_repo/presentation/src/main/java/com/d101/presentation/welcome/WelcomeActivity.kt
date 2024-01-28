package com.d101.presentation.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.d101.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}
