package com.sherazsadiq.i210523

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.IOException

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // Handle the data message here.
            // This is where you can trigger a notification.
            Log.d("FCM", "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data.toString())
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: " + it.body)
            sendNotification(it.body.toString())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Save the new FCM token in your server or local database.
        Log.d("FCM", "Refreshed token: " + token)

        // Send the new token to your server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        token?.let {
            val client = okhttp3.OkHttpClient()

            val requestBody = okhttp3.FormBody.Builder()
                .add("token", it)
                .build()

            val request = okhttp3.Request.Builder()
                .url("https://your-server.com/api/register_token")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("FCM", "Failed to register token", e)
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (!response.isSuccessful) {
                        Log.e("FCM", "Failed to register token: " + response.body?.string())
                    } else {
                        Log.d("FCM", "Successfully registered token")
                    }
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(messageBody: String) {
        val channelId = "channel_id"
        val channelName = "channel_name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel: NotificationChannel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }
}