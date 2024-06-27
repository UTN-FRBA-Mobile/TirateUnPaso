package com.example.tirateunpaso

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import com.example.tirateunpaso.navigation.TirateUnPasoNavigation
import android.Manifest
import android.app.Notification
import androidx.lifecycle.lifecycleScope
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val channelId: String = "tirate.un.paso.notification.channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        askToActivateNotifications()
        setContent {
            TirateUnPaso(::sendNotification)
        }
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

    private fun sendNotification() {

        val app : TirateUnPasoApplication = application as TirateUnPasoApplication
        val context = this

        lifecycleScope.launch {
            app.container.healthAdvicesRepository.getOneStream((0..19).random()).collect {
                opHealthAdvice: HealthAdvice? ->
                opHealthAdvice?.let { healthAdvice: HealthAdvice ->

                    val builder = NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentTitle(healthAdvice.category)
                        .setContentText(healthAdvice.description)
                        .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(healthAdvice.description))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    askToActivateNotifications(builder.build())
                }
            }
        }
    }
}

@Composable
private fun TirateUnPaso(sendNotification: () -> Unit) {
    val navController = rememberNavController()
    TirateUnPasoNavigation(navHostController = navController, sendNotification = sendNotification)
}
