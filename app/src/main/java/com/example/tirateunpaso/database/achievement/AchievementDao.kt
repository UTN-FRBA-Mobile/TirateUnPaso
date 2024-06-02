package com.example.tirateunpaso.database.achievement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface AchievementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(achievement: Achievement)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(vararg achievements: Achievement)

    @Update
    suspend fun updateOne(achievement: Achievement)

    @Delete
    suspend fun deleteOne(achievement: Achievement)

    @Query("SELECT * from achievements WHERE id = :id")
    fun getOne(id: Int): Flow<Achievement>

    @Query("SELECT * from achievements ORDER BY id ASC")
    fun getAllStream(): Flow<List<Achievement>>

    @Query("SELECT * from achievements ORDER BY id ASC")
    fun getAllList(): List<Achievement>

    @Query("DELETE FROM achievements")
    fun deleteAll()
}
