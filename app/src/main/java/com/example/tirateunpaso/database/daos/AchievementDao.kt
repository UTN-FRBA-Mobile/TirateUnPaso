package com.example.tirateunpaso.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tirateunpaso.database.entities.Achievement


@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    fun getAll(): List<Achievement>

    @Query("SELECT * FROM achievement WHERE id IN (:achievementIds)")
    fun loadAllByIds(achievementIds: IntArray): List<Achievement>

    @Insert
    fun insertAll(vararg achievements: Achievement)

    @Delete
    fun delete(achievement: Achievement)
}
