package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData

// Refined Abstractions
class ReminderNotification(sender: NotificationSender) : Notification(sender) {
    override suspend fun notify(data: NotificationData): Result<Unit> {
        require(data.reminderId != null) { "Reminder ID required" }
        return sender.send(data)
    }

    override fun canHandle(channel: NotificationChannel): Boolean =
        sender.supportsChannel(channel)
}