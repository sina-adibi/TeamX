package com.example.task.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class PostEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "saveDate") val saveDate: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "seen") val seen: String
)
