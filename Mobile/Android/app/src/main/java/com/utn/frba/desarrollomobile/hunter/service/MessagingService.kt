package com.utn.frba.desarrollomobile.hunter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity

class MessagingService : FirebaseMessagingService() {

    companion object { var NOTIFICACTION_ID = 0 }

    override  fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            NOTIFICACTION_ID += 1
            sendNotification(remoteMessage.notification?.title!!,
                remoteMessage.notification?.body!!,
                remoteMessage.data?.getValue("game_id").toString()!!
            )
        }
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                R.string.default_notification_channel_id.toString(),
                R.string.app_name.toString(),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }

    fun sendNotification(title:String , messageBody:String, game_id: String) {
        createNotificationChannel()

        val notificationIntent = Intent(this,  MainActivity::class.java)
        notificationIntent.putExtra("game_id", game_id)
        /*notificationIntent.putExtra("notification_id", NOTIFICACTION_ID.toString())*/

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICACTION_ID, notificationIntent, 0
        )

        val newMessageNotification = NotificationCompat.Builder(this, R.string.default_notification_channel_id.toString())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)
            .build()

        //startForeground(1, newMessageNotification);

        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICACTION_ID, newMessageNotification)
        }
    }
}