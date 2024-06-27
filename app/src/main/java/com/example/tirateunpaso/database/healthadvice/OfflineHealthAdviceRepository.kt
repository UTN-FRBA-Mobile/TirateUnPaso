package com.example.tirateunpaso.database.healthadvice

import kotlinx.coroutines.flow.Flow

class OfflineHealthAdviceRepository(private val healthAdviceDao: HealthAdviceDao) : HealthAdvicesRepository {
    override fun getAllList(): List<HealthAdvice> = healthAdviceDao.getAllList()

    override fun getAllStream(): Flow<List<HealthAdvice>> = healthAdviceDao.getAllStream()

    override fun getOneStream(id: Int): Flow<HealthAdvice?> = healthAdviceDao.getOne(id)

    override suspend fun insertOne(healthAdvice: HealthAdvice) = healthAdviceDao.insertOne(healthAdvice)

    override suspend fun deleteOne(healthAdvice: HealthAdvice) = healthAdviceDao.deleteOne(healthAdvice)

    override suspend fun updateOne(healthAdvice: HealthAdvice) = healthAdviceDao.updateOne(healthAdvice)

    override suspend fun deleteAll() = healthAdviceDao.deleteAll()

    override suspend fun insertMany(vararg healthAdvices: HealthAdvice) = healthAdviceDao.insertMany(*healthAdvices)
}
