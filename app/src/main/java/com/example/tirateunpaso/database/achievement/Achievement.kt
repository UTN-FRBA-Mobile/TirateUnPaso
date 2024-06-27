package com.example.tirateunpaso.database.achievement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title", defaultValue = "") val title: String,
    @ColumnInfo(name = "achievement_type", defaultValue = "") val achievementType: String,
    @ColumnInfo(name = "actual_score", defaultValue = "0") val actualScore: Int,
    @ColumnInfo(name = "required_score", defaultValue = "1000") val requiredScore: Int
)
