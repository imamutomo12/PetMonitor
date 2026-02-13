package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import edu.imamutomo.petmonitor.domain.model.notification.NotificationPriority
import javax.inject.Inject

class SMSNotificationSender @Inject constructor(
    private val smsService: SMSService
) : NotificationSender {
    override suspend fun send(notification: NotificationData): Result<Unit> = runCatching {
        require(notification.channel == NotificationChannel.SMS)
        smsService.sendSMS(
            phoneNumber = "0219301293",
            message = "${notification.title}: ${notification.message}"
        )
    }

    override fun supportsChannel(channel: NotificationChannel): Boolean =
        channel == NotificationChannel.SMS

    override fun getPriority(): NotificationPriority = NotificationPriority.HIGH
}