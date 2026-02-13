package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData

abstract class Notification(
    protected val sender: NotificationSender
) {
    abstract suspend fun notify(data: NotificationData): Result<Unit>
    abstract fun canHandle(channel: NotificationChannel): Boolean
}