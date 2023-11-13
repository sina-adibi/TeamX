package com.example.task.utils

import com.example.task.room.PostEntity

interface MessageInsertCallback {
    fun onMessageInserted(postEntity: PostEntity)
}