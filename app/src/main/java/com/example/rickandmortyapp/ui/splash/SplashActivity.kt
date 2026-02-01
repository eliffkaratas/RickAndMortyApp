package com.example.rickandmortyapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rickandmortyapp.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LottieLoading()
        }

        lifecycleScope.launch {
            delay(700)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}