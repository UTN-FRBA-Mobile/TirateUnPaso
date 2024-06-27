package com.example.tirateunpaso.database.healthadvice

import kotlinx.coroutines.flow.Flow

interface HealthAdvicesRepository {
    fun getAllList(): List<HealthAdvice>
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllStream(): Flow<List<HealthAdvice>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getOneStream(id: Int): Flow<HealthAdvice?>

    suspend fun insertOne(healthAdvice: HealthAdvice)

    suspend fun deleteOne(healthAdvice: HealthAdvice)

    suspend fun updateOne(healthAdvice: HealthAdvice)

    suspend fun deleteAll()

    suspend fun insertMany(vararg healthAdvices: HealthAdvice)
}
