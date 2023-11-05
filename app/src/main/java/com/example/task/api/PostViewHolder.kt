package com.example.task.api

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.room.PostEntity

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    private val btSeen: Button = itemView.findViewById(R.id.btSeen)

    fun bindView(postEntity: PostEntity) {
        tvTitle.text = postEntity.saveDate
        tvBody.text = postEntity.message

        if (postEntity.seen == "0") {
            btSeen.text = "پیام خوانده نشده"
        } else if (postEntity.seen == "1") {
            btSeen.text = "پیام خوانده شده"
        } else {
            btSeen.text = "error"
        }

        val sharedPreferences = itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val isButtonClicked = sharedPreferences.getBoolean("isButtonClicked_${postEntity.id}", false)
        if (isButtonClicked) {
            btSeen.text = "پیام خوانده شده"
        }

        btSeen.setOnClickListener {
            btSeen.text = "پیام خوانده شده"
            editor.putBoolean("isButtonClicked_${postEntity.id}", true)
            editor.apply()
        }
    }
}
