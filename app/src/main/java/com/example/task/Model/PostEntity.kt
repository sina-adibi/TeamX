package com.example.task.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.task.Model.Constant.POST_TABLE

@Entity(tableName = POST_TABLE)
data class PostEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "saveDate") val saveDate: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "seen") var seen: String,
    @ColumnInfo(name = "spinnerSelection") var spinnerSelection: String = "DefaultSpinnerValue",
    @ColumnInfo(name = "checkbox1") var checkbox1: Boolean = false,
    @ColumnInfo(name = "checkbox2") var checkbox2: Boolean = false,
    @ColumnInfo(name = "checkbox3") var checkbox3: Boolean = false,
    @ColumnInfo(name = "checkbox4") var checkbox4: Boolean = false,
    @ColumnInfo(name = "checkbox5") var checkbox5: Boolean = false,
    @ColumnInfo(name = "isDeleted") var isDeleted: Boolean = false
)
