package edu.imamutomo.petmonitor.data.notification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.imamutomo.petmonitor.data.local.dao.ReminderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderSchedulingService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reminderDao: ReminderDao
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val scope = CoroutineScope(Dispatchers.IO)

    fun scheduleReminder(reminderId: String, title: String, message: String, triggerTime: Long) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("reminder_id", reminderId)
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelReminder(reminderId: String) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    fun rescheduleAllReminders() {
        scope.launch {
            val currentTime = System.currentTimeMillis()
            reminderDao.getUpcomingReminders(currentTime).collect { reminders ->
                reminders.forEach { entity ->
                    scheduleReminder(
                        reminderId = entity.id,
                        title = entity.title,
                        message = entity.description,
                        triggerTime = entity.scheduledTime
                    )
                }
            }
        }
    }
}