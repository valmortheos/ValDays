package com.valdays.journal.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.valdays.journal.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TITLE") ?: "Task Reminder"
        val ringtoneUriString = intent.getStringExtra("RINGTONE_URI")

        showNotification(context, title, ringtoneUriString)
    }

    private fun showNotification(context: Context, title: String, ringtoneUriString: String?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "valdays_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for task reminders"
                if (ringtoneUriString != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    setSound(Uri.parse(ringtoneUriString), audioAttributes)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            // Use standard icon for now, usually would be R.drawable.ic_notification
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("ValDays Reminder")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (ringtoneUriString != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
             builder.setSound(Uri.parse(ringtoneUriString))
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
