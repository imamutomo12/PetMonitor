package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationAction
import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData

class SystemNotification(sender: NotificationSender) : Notification(sender) {
    override suspend fun notify(data: NotificationData): Result<Unit> {
        // Add system-specific formatting
        val systemData = data.copy(
            title = "[PetCare] ${data.title}",
            actions = listOf(
                NotificationAction("dismiss", "Dismiss", "dismiss"),
                NotificationAction("settings", "Settings", "open_settings")
            )
        )
        return sender.send(systemData)
    }

    override fun canHandle(channel: NotificationChannel): Boolean =
        sender.supportsChannel(channel)
}