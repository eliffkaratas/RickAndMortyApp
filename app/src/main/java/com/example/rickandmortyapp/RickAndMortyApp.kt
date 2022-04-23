package com.example.rickandmortyapp

import android.app.Application
import android.os.Looper
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class RickAndMortyApp : Application() {

    val appScope by lazy {
        CoroutineScope(SupervisorJob())
    }

    override fun onCreate() {
        super.onCreate()
        handleUncaughtException()
    }

    private fun handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    Toast.makeText(applicationContext, R.string.unknown_error, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
                }
            }.start()
        }
    }
}