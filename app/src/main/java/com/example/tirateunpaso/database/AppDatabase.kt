package com.example.tirateunpaso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tirateunpaso.database.daos.AchievementDao
import com.example.tirateunpaso.database.daos.UserDao
import com.example.tirateunpaso.database.entities.Achievement
import com.example.tirateunpaso.database.entities.User

@Database(entities = [Achievement::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun achievementDao(): AchievementDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
