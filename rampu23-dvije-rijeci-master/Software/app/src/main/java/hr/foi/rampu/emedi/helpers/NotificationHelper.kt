package hr.foi.rampu.emedi.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import hr.foi.rampu.emedi.R

class NotificationHelper {
    companion object {
        fun showNotification(context: Context, message: String) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"
            val notificationId = 1
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.lightColor = Color.BLUE
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setContentTitle("eMedi Notification!")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_checkmark)
                .setAutoCancel(true)

            notificationManager.notify(notificationId, builder.build())
        }
    }
}