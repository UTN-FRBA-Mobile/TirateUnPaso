package com.example.tirateunpaso.database.achievement

import kotlinx.coroutines.flow.Flow

interface AchievementsRepository {
    fun getAllList(): List<Achievement>
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllStream(): Flow<List<Achievement>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getOneStream(id: Int): Flow<Achievement?>

    suspend fun insertOne(achievement: Achievement)

    suspend fun deleteOne(achievement: Achievement)

    suspend fun updateOne(achievement: Achievement)

    suspend fun deleteAll()

    suspend fun insertMany(vararg achievements: Achievement)
}
