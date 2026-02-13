package edu.imamutomo.petmonitor.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import edu.imamutomo.petmonitor.R
import edu.imamutomo.petmonitor.data.notification.ReminderBroadcastReceiver
import edu.imamutomo.petmonitor.presentation.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {

    companion object {
        const val CHANNEL_ID = "pet_care_reminders"
        const val CHANNEL_NAME = "Pet Care Reminders"
        const val CHANNEL_DESCRIPTION = "Notifications for pet care schedules"
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, id: String, title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reminder_id", id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification,
                "Mark Done",
                createActionIntent(context, id, "COMPLETE")
            )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.notify(id.hashCode(), builder.build())
    }

    private fun createActionIntent(context: Context, reminderId: String, action: String): PendingIntent {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("reminder_id", reminderId)
            putExtra("action", action)
        }
        return PendingIntent.getBroadcast(
            context,
            (reminderId + action).hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}