package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_screen)

        val message = intent.getStringExtra("post_message")
        val tvmassage: TextView = findViewById(R.id.tvmassage)
        tvmassage.text = message
    }
}