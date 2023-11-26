package com.example.task

import android.app.Application
import com.example.task.Model.PostDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val postDatabase = PostDatabase.getInstance(this)
        // Other initialization tasks, if any
    }
}

