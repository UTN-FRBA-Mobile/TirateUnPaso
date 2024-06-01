package com.example.tirateunpaso.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Achievement(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title", defaultValue = "") val title: String,
    @ColumnInfo(name = "actual_score", defaultValue = "0") val actualScore: Int,
    @ColumnInfo(name = "required_score", defaultValue = "1000") val requiredScore: Int,
    @ColumnInfo(name = "unlocked", defaultValue = "false") val unlocked: Boolean
)
