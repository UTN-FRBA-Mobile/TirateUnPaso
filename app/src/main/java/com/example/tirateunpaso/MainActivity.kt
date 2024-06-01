package com.example.tirateunpaso

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.database.AppDatabase
import com.example.tirateunpaso.database.entities.Achievement
import com.example.tirateunpaso.database.entities.User
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(applicationContext)
        initializeDb(db)

        setContent {
            TirateUnPaso()
        }
    }
}

@Composable
private fun TirateUnPaso() {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController)
}

private fun initializeDb(db: AppDatabase) {
    db.achievementDao().insertAll(
        Achievement(0,"Caminar 1000 pasos", 1000, 1000, true),
        Achievement(1,"Caminar 300 kilómetros", 251, 300, false),
        Achievement(2,"Quemar 700 calorías",  445, 700, false),
        Achievement(3,"Invitar a 5 amigos", 0, 5, false),
        Achievement(4,"Usar la app durante 30 días consecutivos", 18, 30, false)
    )

    db.userDao().insertAll(
        User(0, "Juan", "secreta", 20, 170)
    )
}

