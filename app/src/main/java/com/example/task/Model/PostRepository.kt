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
        seen: String,
        selectedOption: String,
        isChecked: Boolean,
        isChecked2: Boolean,
        isChecked3: Boolean,
        isChecked4: Boolean,
        isChecked5: Boolean,
    ) {
        val postEntity = PostEntity(
            id,
            saveDate,
            message,
            seen,
            selectedOption,
            isChecked,
            isChecked2,
            isChecked3,
            isChecked4,
            isChecked5
        )
        postDao.insertPost(postEntity)
    }

    suspend fun insertMessage(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String,
        selectedOption: String,
        isChecked: Boolean,
        isChecked2: Boolean,
        isChecked3: Boolean,
        isChecked4: Boolean,
        isChecked5: Boolean,
    ) {

        val newPost = PostEntity(
            id,
            saveDate,
            message,
            seen,
            selectedOption,
            isChecked,
            isChecked2,
            isChecked3,
            isChecked4,
            isChecked5
        )
        PostRepository.postDao.insertPost(newPost)
    }
}

