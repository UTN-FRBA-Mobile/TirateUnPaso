package com.example.tirateunpaso.database

import android.content.Context
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import com.example.tirateunpaso.database.achievement.OfflineAchievementsRepository
import com.example.tirateunpaso.database.user.OfflineUsersRepository
import com.example.tirateunpaso.database.user.UsersRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val achievementsRepository: AchievementsRepository
    val usersRepository: UsersRepository
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
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(AppDatabase.getDatabase(context).userDao())
    }
}
