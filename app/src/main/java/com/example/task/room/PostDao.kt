package com.example.task.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)

    @Query("SELECT * FROM post_table")
    fun getAllPosts(): LiveData<PostEntity>?
    @Query("SELECT COUNT(*) FROM post_table")
    fun getPostCount(): LiveData<Int>
}



