package com.example.tirateunpaso

import TirateUnPasoTheme
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.example.tirateunpaso.viewmodel.StepCounterVM
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.health.connect.client.permission.HealthPermission

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation
import android.Manifest
import android.app.Notification
import androidx.health.connect.client.HealthConnectClient

import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

private const val REQUEST_CODE_PERMISSIONS = 101
data class RawData(val date: Date, val value: Int)

object DataGenerator {
    fun generateRawGraphData(): List<RawData> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val data = mutableListOf<RawData>()

        // Generar 35 días de datos
        repeat(35) {
            val date = today.clone() as Calendar
            date.add(Calendar.DAY_OF_YEAR, -it) // Retroceder i días desde hoy

            // Determinar el valor de cada día para testear
            val value = when (it) {
                0 -> 0
                6 -> 250  // hoy + 6 valor 250
                1 -> 0
                7, 14 -> 100 // Hoy + 7 y hoy + 14 valor 100
                else -> 1000 // 1000 en todos los demás días
            }

            data.add(RawData(date.time, value))
        }

        return data
    }
}

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

    private val channelId: String = "tirate.un.paso.notification.channel"

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

        loadData()
        sessionStarted = true

        GlobalScope.launch {
            checkPermissionsAndRun(healthConnectClient)
        }

        createNotificationChannel()
        askToActivateNotifications()
        setContent {
            TirateUnPasoTheme {
                TirateUnPaso(::sendNotification, VM)
            }
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
        val steps = sessionSteps
        //sessionSteps son todos los pasos que tiene registrado el sensor, stepsStartSession son los pasos que tenia cuando comenzo la sesion. Los pasos que di en esta sesion es la diferencia de ambos
        saveData(steps)
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val name = "tirate.un.paso.notification.channel"
        val descriptionText = "Tirate un paso notification channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun askToActivateNotifications(opNotification: Notification? = null) {
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
                }
                return@with
            }

            opNotification?.let { notification ->
                cancel(1)
                notify(1, notification)
            }
        }
    }

    private fun sendNotification(opHealthAdvice: HealthAdvice?) {
        val context = this

        lifecycleScope.launch {
            opHealthAdvice?.let { healthAdvice: HealthAdvice ->
                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentTitle(healthAdvice.category)
                    .setContentText(healthAdvice.description)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(healthAdvice.description)
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                askToActivateNotifications(builder.build())
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
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
            if(steps < 1){
                return
            }
            val stepsRecord = StepsRecord(
                count = steps,
                startTime = sessionStart,
                endTime = Instant.now(),
                startZoneOffset = null,
                endZoneOffset = null,
            )
            healthConnectClient.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            Toast.makeText(this, "Error while saving steps", Toast.LENGTH_SHORT).show()
            Log.e("HEALTCONNECTCLIENT", e.toString())
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
        GlobalScope.launch {
            insertSteps(healthConnectClient, steps, sessionStart)
        }
    }

    fun loadData(){
        GlobalScope.launch {
            todaySteps = getTodaySteps(healthConnectClient)
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

@Composable
private fun TirateUnPaso(sendNotification: (HealthAdvice?) -> Unit, stepCounterVM : StepCounterVM) {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController, sendNotification = sendNotification, stepCounterVM = stepCounterVM)
}
