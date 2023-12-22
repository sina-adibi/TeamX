package com.example.task.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.task.Model.PostDao
import com.example.task.View.ViewPager

class PostDeleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        lateinit var postDao: PostDao
        val deletedPostCount = intent?.getIntExtra("deletedPostCount",postDao.getDeletedPostCount()) ?: 0
        (context as? ViewPager)?.refreshDeletedPostCount(deletedPostCount)
    }
}
