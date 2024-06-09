package com.example.tirateunpaso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tirateunpaso.database.achievement.AchievementDao
import com.example.tirateunpaso.database.achievement.Achievement
import com.example.tirateunpaso.database.user.User
import com.example.tirateunpaso.database.user.UserDao


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Achievement::class, User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
