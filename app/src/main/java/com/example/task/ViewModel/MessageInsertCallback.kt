package com.example.task.ViewModel

import com.example.task.Model.PostEntity

interface MessageInsertCallback {
    fun onMessageInserted(postEntity: PostEntity)

}