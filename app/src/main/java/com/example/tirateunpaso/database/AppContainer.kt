package com.example.tirateunpaso.database

import android.content.Context
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import com.example.tirateunpaso.database.achievement.OfflineAchievementsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val achievementsRepository: AchievementsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAchievementsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AchievementsRepository]
     */
    override val achievementsRepository: AchievementsRepository by lazy {
        OfflineAchievementsRepository(InventoryDatabase.getDatabase(context).achievementDao())
    }
}
