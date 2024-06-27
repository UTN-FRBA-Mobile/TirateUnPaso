package com.example.tirateunpaso.database

import android.content.Context
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import com.example.tirateunpaso.database.achievement.OfflineAchievementsRepository
import com.example.tirateunpaso.database.healthadvice.HealthAdvicesRepository
import com.example.tirateunpaso.database.healthadvice.OfflineHealthAdviceRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val achievementsRepository: AchievementsRepository
    val healthAdvicesRepository: HealthAdvicesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAchievementsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AchievementsRepository]
     */
    override val achievementsRepository: AchievementsRepository by lazy {
        OfflineAchievementsRepository(AppDatabase.getDatabase(context).achievementDao())
    }
    /**
     * Implementation for [HealthAdvicesRepository]
     */
    override val healthAdvicesRepository: HealthAdvicesRepository by lazy {
        OfflineHealthAdviceRepository(AppDatabase.getDatabase(context).healthAdviceDao())
    }
}
