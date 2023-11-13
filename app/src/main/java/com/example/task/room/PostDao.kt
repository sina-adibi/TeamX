package com.example.task.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.room.Constant.POST_TABLE

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(post: PostEntity)

    @Query("UPDATE $POST_TABLE SET seen = :newSeen WHERE id = :postId")
    suspend fun updatePostSeen(postId: Int, newSeen: String)

    @Query("SELECT * FROM $POST_TABLE")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query("SELECT COUNT(*) FROM $POST_TABLE")
    fun getPostCount(): LiveData<Int>

    @Query("DELETE FROM $POST_TABLE WHERE id = :postId")
    suspend fun deletePost(postId: Int)


}



