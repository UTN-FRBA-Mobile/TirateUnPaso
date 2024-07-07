package com.example.tirateunpaso.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import com.example.tirateunpaso.ui.screens.HomeScreen
import com.example.tirateunpaso.ui.routes
import com.example.tirateunpaso.ui.screens.AchievementsScreen
import com.example.tirateunpaso.ui.screens.StatisticsScreen

@Composable
fun TirateUnPasoNavigation(navHostController: NavHostController, sendNotification: (HealthAdvice?) -> Unit) {
    NavHost(
        navController = navHostController,
        startDestination = routes.app_flow
    ){
        navigation(startDestination = routes.home, route = routes.app_flow){
            composable(route = routes.home){
                HomeScreen(
                    onShowHealthAdviceClick = sendNotification,
                    onStatisticsClick = {
                        navHostController.navigateToSingleTop(
                            routes.statistics
                        )
                    },
                    onAchievementsClick = {
                        navHostController.navigateToSingleTop(
                            routes.achievements
                        )
                    }
                )
            }
            composable(route = routes.achievements){
                AchievementsScreen(
                    onHomeClick = {
                        navHostController.navigateToSingleTop(
                            routes.home
                        )
                    }
                )
            }
            composable(route = routes.statistics){
                StatisticsScreen(
                    onHomeClick = {
                        navHostController.navigateToSingleTop(
                            routes.home
                        )
                    }
                )
            }
        }
    }
}

fun NavController.navigateToSingleTop(route:String){
    navigate(route){
        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}