package com.example.tirateunpaso.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tirateunpaso.ui.login.LoginScreen
import com.example.tirateunpaso.ui.main.HomeScreen
import com.example.tirateunpaso.ui.routes
import com.example.tirateunpaso.ui.signup.SignUpScreen

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
                            routes.home
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
                            routes.login
                        )
                    },
                    onSignUpClick = {
                        navHostController.navigate(
                            routes.home
                        ){
                            popUpTo(
                                route = routes.login_flow
                            )
                        }
                    }
                )
            }

        }
        composable(route = routes.home){
            HomeScreen()
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