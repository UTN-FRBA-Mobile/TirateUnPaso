package com.example.tirateunpaso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.ui.login.LoginScreen
import com.example.tirateunpaso.ui.main.HomeScreen
import com.example.tirateunpaso.ui.routes
import com.example.tirateunpaso.ui.signup.SignUpScreen

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
    NavHost(navController = navController, startDestination = "login", builder = {
        composable(routes.login){
            LoginScreen(navController)
        }
        composable(routes.signup){
            SignUpScreen(navController)
        }
        composable(routes.home){
            HomeScreen(navController)
        }
    })

}