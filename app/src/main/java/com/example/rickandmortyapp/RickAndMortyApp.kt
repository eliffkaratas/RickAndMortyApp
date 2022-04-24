package com.example.rickandmortyapp

import android.app.Application
import android.os.Looper
import com.example.rickandmortyapp.util.showToast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        handleUncaughtException()
    }

    private fun handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    showToast(getString(R.string.unknown_error))
                    Looper.loop()
                }
            }.start()
        }
    }
}