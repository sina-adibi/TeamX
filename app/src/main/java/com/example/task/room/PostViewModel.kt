package com.example.task.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    var liveDataPost: LiveData<PostEntity>? = null

    suspend fun insertData(postDao: PostDao, id: Int, saveDate: String, message: String, seen: String) {
        PostRepository.insertData(postDao, id, saveDate, message, seen)
    }

    fun getAllPosts(postDao: PostDao): LiveData<PostEntity> {
        liveDataPost = PostRepository.getAllPosts(postDao)
        return liveDataPost as LiveData<PostEntity>
    }

}