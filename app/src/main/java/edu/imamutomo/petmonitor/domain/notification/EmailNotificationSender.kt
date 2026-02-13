package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import edu.imamutomo.petmonitor.domain.model.notification.NotificationPriority
import javax.inject.Inject

class EmailNotificationSender @Inject constructor(
    private val emailService: EmailService
) : NotificationSender {
    override suspend fun send(notification: NotificationData): Result<Unit> = runCatching {
        emailService.sendEmail(
            subject = notification.title,
            body = notification.message,
            recipient = "email" // From user preferences
        )
    }

    override fun supportsChannel(channel: NotificationChannel): Boolean =
        channel == NotificationChannel.EMAIL

    override fun getPriority(): NotificationPriority = NotificationPriority.NORMAL
}
