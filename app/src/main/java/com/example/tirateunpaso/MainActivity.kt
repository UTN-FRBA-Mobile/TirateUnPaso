package com.example.tirateunpaso

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation
import com.example.tirateunpaso.viewmodel.StepCounterVM


class MainActivity : AppCompatActivity(){
    private val VM : StepCounterVM by viewModels()
    private lateinit var stepSensorManager : StepSensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepSensorManager = StepSensorManager(VM)
        stepSensorManager.onCreate(savedInstanceState)
        stepSensorManager.checkPermissions()
//        stepSensorManager.loadData()

        setContent {
            TirateUnPaso(VM)
        }
    }

    override fun onResume(){
        super.onResume()
//        stepSensorManager.registerListener()
    }

    override fun onPause(){
        super.onPause()
//        stepSensorManager.unregisterListener()
    }

    override fun onDestroy() {
//        stepSensorManager.unregisterListener()
        super.onDestroy()
    }

}

@Composable
private fun TirateUnPaso(stepCounterVM : StepCounterVM) {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController, stepCounterVM)
}