package com.example.task.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.task.Model.Constant.POST_TABLE

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(post: PostEntity)
    @Update
    suspend fun updatePost(post: PostEntity)
    @Query("UPDATE $POST_TABLE SET seen = :newSeen WHERE id = :postId")
    suspend fun updatePostSeen(postId: Int, newSeen: String)

    @Query("SELECT * FROM $POST_TABLE WHERE isDeleted = 0")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query("SELECT COUNT(*) FROM $POST_TABLE")
    fun getPostCount(): LiveData<Int>

    @Query("DELETE FROM $POST_TABLE WHERE id = :postId")
    suspend fun deletePost(postId: Int)

    @Query("SELECT * FROM $POST_TABLE WHERE isDeleted = 0")
    fun getAllNonDeletedPosts(): LiveData<List<PostEntity>>

    @Query("UPDATE $POST_TABLE SET isDeleted = 1 WHERE id = :postId")
    suspend fun softDeletePost(postId: Int)
    @Query("SELECT * FROM $POST_TABLE WHERE isDeleted = 1")
    fun getDeletedPosts(): LiveData<List<PostEntity>>
    @Query("SELECT COUNT(*) FROM $POST_TABLE WHERE isDeleted = 1")
    fun getDeletedPostCount(): LiveData<Int>

    @Query("SELECT * FROM $POST_TABLE WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?
}




