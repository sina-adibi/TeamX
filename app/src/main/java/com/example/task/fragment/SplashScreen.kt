package com.example.task.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import com.example.task.MainActivity
import com.example.task.R
import androidx.navigation.fragment.findNavController
class SplashScreen : Fragment() {

    private val SPLASH_DURATION = 3000L // Duration of splash screen in milliseconds

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DURATION)

        return view
    }

    private fun navigateToNextScreen() {
        val action = SplashScreenDirections.actionSplashToLogin()
        findNavController().navigate(action)
    }
}

