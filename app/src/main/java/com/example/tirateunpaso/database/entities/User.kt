package com.example.tirateunpaso.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "username", defaultValue = "username") val username: String,
    @ColumnInfo(name = "hashed_password", defaultValue = "pass") val hashed_password: String,
    @ColumnInfo(name = "age", defaultValue = "100") val age: Int,
    @ColumnInfo(name = "height", defaultValue = "200") val height: Int
)
