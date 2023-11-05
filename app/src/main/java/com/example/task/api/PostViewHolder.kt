package com.example.task.api

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.room.PostDao
import com.example.task.room.PostEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    private val btSeen: Button = itemView.findViewById(R.id.btSeen)

    fun bindView(postEntity: PostEntity, postDao: PostDao) {
        tvTitle.text = postEntity.saveDate
        tvBody.text = postEntity.message

        if (postEntity.seen == "0") {
            btSeen.text = "پیام خوانده نشده"
        } else if (postEntity.seen == "1") {
            btSeen.text = "پیام خوانده شده"
        } else {
            btSeen.text = "error"
        }

        btSeen.setOnClickListener {
            val updatedSeenValue = if (postEntity.seen == "0") "1" else "1"
            postEntity.seen = updatedSeenValue

            CoroutineScope(Dispatchers.IO).launch {
                postDao.updatePostSeen(postEntity.id, updatedSeenValue)
            }
            btSeen.text = "پیام خوانده شده"
        }
    }
}