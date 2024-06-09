package com.example.tirateunpaso.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Constructor

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "username", defaultValue = "username") val username: String,
    @ColumnInfo(name = "hashed_password", defaultValue = "pass") val hashedPassword: String,
    @ColumnInfo(name = "age", defaultValue = "100") val age: Int,
    @ColumnInfo(name = "height", defaultValue = "200") val height: Int
)
