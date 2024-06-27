package com.example.tirateunpaso.database.healthadvice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface HealthAdviceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(healthAdvice: HealthAdvice)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(vararg healthAdvices: HealthAdvice)

    @Update
    suspend fun updateOne(healthAdvice: HealthAdvice)

    @Delete
    suspend fun deleteOne(healthAdvice: HealthAdvice)

    @Query("SELECT * from healthadvices WHERE id = :id")
    fun getOne(id: Int): Flow<HealthAdvice>

    @Query("SELECT * from healthadvices ORDER BY id ASC")
    fun getAllStream(): Flow<List<HealthAdvice>>

    @Query("SELECT * from healthadvices ORDER BY id ASC")
    fun getAllList(): List<HealthAdvice>

    @Query("DELETE FROM healthadvices")
    fun deleteAll()
}
