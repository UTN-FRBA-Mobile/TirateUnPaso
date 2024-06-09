package com.example.tirateunpaso.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(vararg users: User)

    @Update
    suspend fun updateOne(user: User)

    @Delete
    suspend fun deleteOne(user: User)

    @Query("SELECT * from users WHERE id = :id")
    fun getOne(id: Int): Flow<User>

    @Query("SELECT * from users ORDER BY id ASC")
    fun getAllStream(): Flow<List<User>>

    @Query("SELECT * from users ORDER BY id ASC")
    fun getAllList(): List<User>

    @Query("DELETE FROM users")
    fun deleteAll()
}