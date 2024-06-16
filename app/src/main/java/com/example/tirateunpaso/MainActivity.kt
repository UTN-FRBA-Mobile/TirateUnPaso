package com.example.tirateunpaso

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation
import com.example.tirateunpaso.viewmodel.StepCounterVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalAmount

private const val REQUEST_CODE_PERMISSIONS = 101

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val sensorManager: SensorManager by lazy {
        this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val stepSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private var todaySteps : Long = 0
    private var sessionSteps : Long = 0
    private var stepsStartSession : Long = 0
    private var sessionStarted  = false
    private var sessionStart : Instant = Instant.now()

    private val VM : StepCounterVM by viewModels()

    val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class)
        )

    private val healthConnectClient : HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val availabilityStatus = HealthConnectClient.getSdkStatus(applicationContext, "com.google.android.apps.healthdata")
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return // early return as there is no viable integration
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            return
        }

        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
            } else {
                // Lack of required permissions
            }
        }

        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions already granted; proceed with inserting or reading data
            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }

        loadData() //TODO: no se si es necesario cargar la data aca, ya que se hace en onResume()
        sessionStarted = true

        GlobalScope.launch {
            checkPermissionsAndRun(healthConnectClient) //TODO: los permisos deberian controlarse todo el tiempo
        }

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
        loadData()
        sessionStarted = true
        sessionStart = Instant.now()
    }

    override fun onPause(){
        super.onPause()
        //sessionSteps son todos los pasos que tiene registrado el sensor, stepsStartSession son los pasos que tenia cuando comenzo la sesion. Los pasos que di en esta sesion es la diferencia de ambos
        saveData(sessionSteps - stepsStartSession)
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?    ) {
        if (event!!.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            val sensorSteps = event.values[0].toLong()
            if(sessionStarted){
                sessionStarted = false
                stepsStartSession = sensorSteps
            }
            sessionSteps = sensorSteps - stepsStartSession
            VM.UpdateSteps(todaySteps + sessionSteps)
        }
    }

    suspend fun insertSteps(healthConnectClient: HealthConnectClient, steps: Long, sessionStart: Instant) {
        try {
            val stepsRecord = StepsRecord(
                count = steps,
                startTime = sessionStart,
                endTime = Instant.now(),
                startZoneOffset = null,
                endZoneOffset = null,
            )
            healthConnectClient.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            // Run error handling here
        }
    }

    suspend fun aggregateSteps(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) : Long {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[StepsRecord.COUNT_TOTAL]
            return stepCount!!
        } catch (e: Exception) {
            // Run error handling here
            return 0
        }
    }

    suspend fun getTodaySteps(healthConnectClient: HealthConnectClient) : Long{
        val localDate = LocalDate.now()   // your current date time
        val startOfDay: LocalDateTime = localDate.atStartOfDay() // date time at start of the date
        val startOfDayInstant = startOfDay.atZone(ZoneId.systemDefault()).toInstant()
        return aggregateSteps(healthConnectClient, startOfDayInstant, Instant.now())
    }

    fun saveData(steps: Long){
        runBlocking {
            insertSteps(healthConnectClient, steps, sessionStart)
        }
    }

    fun loadData(){
        runBlocking {
            todaySteps = getTodaySteps(healthConnectClient)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

@Composable
private fun TirateUnPaso(stepCounterVM : StepCounterVM) {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController, stepCounterVM)
}