package edu.imamutomo.petmonitor.domain.notification

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import javax.inject.Inject

class NotificationManager @Inject constructor(
    private val senders: Set<@JvmSuppressWildcards NotificationSender>
) {
    suspend fun sendNotification(
        data: NotificationData,
        preferredChannel: NotificationChannel = NotificationChannel.PUSH
    ): Result<Unit> {
        // Find best sender for channel
        val sender = senders.find { it.supportsChannel(preferredChannel) }
            ?: senders.maxByOrNull { it.getPriority().ordinal }
            ?: return Result.failure(IllegalStateException("No notification sender available"))

        return sender.send(data)
    }

    suspend fun broadcastNotification(data: NotificationData): List<Result<Unit>> {
        return senders.map { it.send(data) }
    }
}
