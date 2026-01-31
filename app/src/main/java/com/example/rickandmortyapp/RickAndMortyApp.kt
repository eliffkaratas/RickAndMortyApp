package com.example.rickandmortyapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("Crash", "Uncaught exception", throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private val defaultHandler =
        Thread.getDefaultUncaughtExceptionHandler()
}