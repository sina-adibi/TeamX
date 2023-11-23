package com.example.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            installSplashScreen()
//            delay(1000)
        }
        setContentView(R.layout.activity_splash_screen)
        runBlocking {
            MainScope().launch {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                delay(1000)
                startActivity(intent)
                finish()
            }
        }

    }
}