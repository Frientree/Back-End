package com.d101.frientree

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.d101.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Intent().also { intent ->
            intent.action = "FCM_NEW_TOKEN"
            intent.putExtra("TOKEN", token)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        var messageTitle = ""
        var messageContent = ""

        message.notification?.let {
            messageTitle = it.title.toString()
            messageContent = it.body.toString()
        } ?: run {
            message.data.isNotEmpty().let {
                Log.d("FCM", "data.message: ${message.data}")
                messageTitle = message.data["title"].toString()
                messageContent = message.data["body"].toString()
            }
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            pendingIntentFlags,
        )

        val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setSmallIcon(com.d101.presentation.R.drawable.img_main_tree)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 없으므로 알림을 보낼 수 없습니다.
                // 이 경우, 알림 권한을 요청하는 로직을 액티비티에 구현해야 합니다.
                Log.d("FCM", "POST_NOTIFICATIONS permission is not granted")
                return
            }
        }

        NotificationManagerCompat.from(this).notify(101, builder.build())
    }
}
