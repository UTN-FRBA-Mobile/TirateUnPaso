package com.example.tirateunpaso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tirateunpaso.database.achievement.AchievementDao
import com.example.tirateunpaso.database.achievement.Achievement


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Achievement::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "achievement_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
