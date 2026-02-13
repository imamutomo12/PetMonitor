package edu.imamutomo.petmonitor.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import edu.imamutomo.petmonitor.domain.notification.NotificationHelper
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id") ?: return
        val title = intent.getStringExtra("title") ?: "Pet Reminder"
        val message = intent.getStringExtra("message") ?: "Time to care for your pet!"

        notificationHelper.showNotification(context, reminderId, title, message)
    }
}