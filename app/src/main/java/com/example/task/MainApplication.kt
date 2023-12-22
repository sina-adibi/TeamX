package com.example.task

import android.app.Application
import firebase.com.protolitewrapper.BuildConfig
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
