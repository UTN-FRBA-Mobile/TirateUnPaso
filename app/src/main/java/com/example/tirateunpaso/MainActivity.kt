package com.example.tirateunpaso

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation
import com.example.tirateunpaso.viewmodel.StepCounterVM

private const val REQUEST_CODE_PERMISSIONS = 101

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val sensorManager: SensorManager by lazy {
        this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val stepSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private var appCreated = false
    private var currentSteps : Long = 0
    private var sensorCarry : Long = 0
    private var previousTotalSteps : Long = 0
    private var sensorSteps : Long = 0

    private val VM : StepCounterVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        appCreated = true

        setContent {
            TirateUnPaso(VM)
        }
        requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_PERMISSIONS) // Request permission if needed
    }

    override fun onResume(){
        super.onResume()
        if (stepSensor == null){
            Toast.makeText(this, "This device has no step sensor", Toast.LENGTH_SHORT).show()
        }
        else{
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause(){
        super.onPause()
        saveData()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            sensorSteps = event.values[0].toLong()
            if(appCreated){
                appCreated = false
                sensorCarry = sensorSteps
            }
            currentSteps = previousTotalSteps + sensorSteps - sensorCarry
            VM.UpdateSteps(currentSteps)
        }
    }

    fun resetSteps(){
        currentSteps = 0
        sensorCarry = sensorSteps
        saveData()
    }

    fun saveData(){
        val sharedPref = getSharedPreferences("stepCounterPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putLong("key1", currentSteps)
        editor.apply()
    }

    fun loadData(){
        val sharedPref = getSharedPreferences("stepCounterPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPref.getLong("key1", 0L)
        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

@Composable
private fun TirateUnPaso(stepCounterVM : StepCounterVM) {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController, stepCounterVM)
}