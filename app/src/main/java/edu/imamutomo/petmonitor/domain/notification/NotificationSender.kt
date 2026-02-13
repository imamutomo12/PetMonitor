package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import edu.imamutomo.petmonitor.domain.model.notification.NotificationPriority

interface NotificationSender {
    suspend fun send(notification: NotificationData): Result<Unit>
    fun supportsChannel(channel: NotificationChannel): Boolean
    fun getPriority(): NotificationPriority
}