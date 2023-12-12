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
        seen: String,
        selectedOption:String,
        isChecked:Boolean,
        isChecked2:Boolean,
        isChecked3:Boolean,
        isChecked4:Boolean,
        isChecked5:Boolean,
    ) {
        PostRepository.insertData(postDao, id, saveDate, message, seen,selectedOption,isChecked,isChecked2,isChecked3,isChecked4,isChecked5)
    }
    suspend fun insertMessage(
        postDao: PostDao,
        id: Int,
        saveDate: String,
        message: String,
        seen: String,
        selectedOption:String,
        isChecked:Boolean,
        isChecked2:Boolean,
        isChecked3:Boolean,
        isChecked4:Boolean,
        isChecked5:Boolean,
    ) {
        PostRepository.insertMessage(postDao, id, saveDate, message, seen,selectedOption,isChecked,isChecked2,isChecked3,isChecked4,isChecked5)
    }

    fun getAllPosts(postDao: PostDao): LiveData<PostEntity>? {
        return postDao.getAllPosts().map { posts ->
            posts.firstOrNull()
        } as LiveData<PostEntity>?
    }
}