package com.example.task.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.task.Model.PostDao
import com.example.task.Model.PostEntity
import com.example.task.Model.PostRepository

class PostViewModel : ViewModel() {

    var liveDataPost: LiveData<PostEntity>? = null

    suspend fun insertData(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String
    ) {
        PostRepository.insertData(postDao, id, saveDate, message, seen)
    }
    suspend fun insertMessage(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String
    ) {
        PostRepository.insertMessage(postDao, id, saveDate, message, seen)
    }

    fun getAllPosts(postDao: PostDao): LiveData<PostEntity>? {
        return postDao.getAllPosts().map { posts ->
            posts.firstOrNull()
        } as LiveData<PostEntity>?
    }
}