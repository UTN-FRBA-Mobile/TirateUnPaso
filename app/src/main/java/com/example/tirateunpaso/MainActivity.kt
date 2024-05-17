package com.example.tirateunpaso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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