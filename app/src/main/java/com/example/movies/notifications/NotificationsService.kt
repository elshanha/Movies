package com.example.movies.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.movies.R
import com.example.movies.login.sign_in.GoogleAuthUiClient
import com.example.movies.main.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient

const val NOTIFICATION_CHANNEL_ID = "CH-1"
const val NOTIFICATION_CHANNEL_NAME = "Login"
const val NOTIFICATION_ID = 1
const val REQUEST_CODE = 200

interface NotificationsService {
    fun showNotification(value: String)
    fun createNotificationChannel()
    fun hideNotification()
}

class NotificationsServiceImpl(
    private val context: Context,
) : NotificationsService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val myIntent = Intent(
        context, MainActivity::class.java
    )

    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    override fun showNotification(value: String) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie)
            .setContentTitle(value)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }


}