package com.example.task.room

import com.example.task.room.PostEntity

interface MessageInsertCallback {
    fun onMessageInserted(postEntity: PostEntity)
}