package com.example.task.Model

import androidx.lifecycle.LiveData

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

    suspend fun insertMessage(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String
    ) {

        val newPost = PostEntity(id, saveDate, message, seen)
        PostRepository.postDao.insertPost(newPost)
    }
}
