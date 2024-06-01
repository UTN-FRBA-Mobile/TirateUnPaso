package com.example.tirateunpaso.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tirateunpaso.database.AppDatabase
import com.example.tirateunpaso.ui.screens.LoginScreen
import com.example.tirateunpaso.ui.screens.HomeScreen
import com.example.tirateunpaso.ui.routes
import com.example.tirateunpaso.ui.screens.AchievementsScreen
import com.example.tirateunpaso.ui.screens.SignUpScreen
import com.example.tirateunpaso.ui.screens.StatisticsScreen

@Composable
fun TirateUnPasoNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = routes.login_flow
    ){
        navigation(startDestination = routes.login, route = routes.login_flow){
            composable(route = routes.login){
                LoginScreen(
                    onLoginClick = {
                        navHostController.navigate(
                            routes.app_flow
                        ){
                            popUpTo(
                                route = routes.login_flow
                            )
                        }
                    },
                    onSignUpClick = {
                        navHostController.navigateToSingleTop(
                            routes.signup
                        )
                    }
                )
            }
            composable(route = routes.signup){
                SignUpScreen(
                    onLoginClick = {
                        navHostController.navigateToSingleTop(
                            routes.login_flow
                        )
                    },
                    onSignUpClick = {
                        navHostController.navigate(
                            routes.app_flow
                        ){
                            popUpTo(
                                route = routes.login_flow
                            )
                        }
                    }
                )
            }
        }
        navigation(startDestination = routes.home, route = routes.app_flow){
            composable(route = routes.home){
                HomeScreen(
                    onLogoutClick = {
                        navHostController.navigate(
                            routes.login_flow
                        ){
                            popUpTo(
                                route = routes.app_flow
                            )
                        }
                    },
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