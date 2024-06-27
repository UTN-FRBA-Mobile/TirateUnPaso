package com.example.tirateunpaso.database.healthadvice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "healthadvices")
data class HealthAdvice(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category", defaultValue = "") val category: String,
    @ColumnInfo(name = "description", defaultValue = "") val description: String,
)
