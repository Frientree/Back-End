package com.d101.frientree

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.d101.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        MainActivity.uploadToken(token)
    }
    override fun onMessageReceived(message: RemoteMessage) {
        var messageTitle = ""
        var messageContent = ""

        if(message.notification != null){
            //foreground
            messageTitle= message.notification!!.title.toString()
            messageContent = message.notification!!.body.toString()

        }else{
            val data = message.data
            Log.d("FCM", "data.message: $data")
            Log.d("FCM", "data.message: ${data["title"]}")
            Log.d("FCM", "data.message: ${data["body"]}")

            messageTitle = data["title"].toString()
            messageContent = data["body"].toString()
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder1 = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

    }
}
