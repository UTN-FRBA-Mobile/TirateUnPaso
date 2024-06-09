package com.example.tirateunpaso.database.achievement

import kotlinx.coroutines.flow.Flow

class OfflineAchievementsRepository(private val achievementDao: AchievementDao) : AchievementsRepository {
    override fun getAllList(): List<Achievement> = achievementDao.getAllList()

    override fun getAllStream(): Flow<List<Achievement>> = achievementDao.getAllStream()

    override fun getOneStream(id: Int): Flow<Achievement?> = achievementDao.getOne(id)

    override suspend fun insertOne(achievement: Achievement) = achievementDao.insertOne(achievement)

    override suspend fun deleteOne(achievement: Achievement) = achievementDao.deleteOne(achievement)

    override suspend fun updateOne(achievement: Achievement) = achievementDao.updateOne(achievement)

    override suspend fun deleteAll() = achievementDao.deleteAll()

    override suspend fun insertMany(vararg achievements: Achievement) = achievementDao.insertMany(*achievements)
}
