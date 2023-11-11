package com.example.task.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.task.room.Constant.POST_TABLE

@Entity(tableName = POST_TABLE)
data class PostEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "saveDate") val saveDate: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "seen") var seen: String
)
