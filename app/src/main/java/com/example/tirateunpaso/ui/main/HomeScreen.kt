package com.example.tirateunpaso.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController? = null) {
    Text(text = "This is home")
}

@Preview
@Composable
fun DefaultHomePreview() {
    HomeScreen()
}