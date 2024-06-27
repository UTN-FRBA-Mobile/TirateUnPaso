package com.example.tirateunpaso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tirateunpaso.database.achievement.AchievementDao
import com.example.tirateunpaso.database.achievement.Achievement
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import com.example.tirateunpaso.database.healthadvice.HealthAdviceDao


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Achievement::class, HealthAdvice::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao
    abstract fun healthAdviceDao(): HealthAdviceDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "tirateunpaso.db")
                    .createFromAsset("tirateunpaso.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
