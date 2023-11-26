package com.example.task.View

import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.example.task.R
import androidx.navigation.fragment.findNavController
import androidx.test.core.app.ApplicationProvider

class LoginVM : ViewModel() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    fun saveCredentials(context: Context, email: String, password: String) {
        val sharedpreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedpreferences.edit()
        editor.putString(EMAIL_KEY, email)
        editor.putString(PASSWORD_KEY, password)
        editor.apply()
    }

    fun getSavedEmail(context: Context): String? {
        val sharedpreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedpreferences.getString(EMAIL_KEY, null)
    }

    fun getSavedPassword(context: Context): String? {
        val sharedpreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedpreferences.getString(PASSWORD_KEY, null)
    }

    fun hasSavedCredentials(context: Context): Boolean {
        return getSavedEmail(context) != null && getSavedPassword(context) != null
    }
}

