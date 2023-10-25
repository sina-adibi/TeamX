package com.example.task

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView

class HomeActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        email = sharedpreferences.getString(EMAIL_KEY, null)

        val welcomeTV = findViewById<TextView>(R.id.idTVWelcome)
        welcomeTV.text = "Welcome $email"
        val logoutBtn = findViewById<Button>(R.id.idBtnLogout)
        logoutBtn.setOnClickListener {
            val editor = sharedpreferences.edit()

            editor.clear()
            editor.apply()

            val i = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

//        val composeView = findViewById<ComposeView>(R.id.composeView)
//        val apiBtn = findViewById<Button>(R.id.ApiBtn)
//        apiBtn.setOnClickListener {
//            composeView.setContent {
//                Text(text = "Hello from Compose!")
//            }
//        }

    }
}
