package com.example.task.room

import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object PostRepository {
    private lateinit var postDao: PostDao

    fun getAllPosts(postDao: PostDao): LiveData<List<PostEntity>> {
        return postDao.getAllPosts()
    }

    suspend fun insertData(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String
    ) {
        val postEntity = PostEntity(id, saveDate, message, seen)
        postDao.insertPost(postEntity)
    }
}