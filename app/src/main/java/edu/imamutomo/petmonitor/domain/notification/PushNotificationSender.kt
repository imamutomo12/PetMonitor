package edu.imamutomo.petmonitor.domain.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.imamutomo.petmonitor.R
import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import edu.imamutomo.petmonitor.domain.model.notification.NotificationPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PushNotificationSender @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) : NotificationSender {

    override suspend fun send(notification: NotificationData): Result<Unit> = runCatching {
        // Android Notification implementation
        val builder = NotificationCompat.Builder(context, notification.channel.name)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        withContext(Dispatchers.Main) {
            notificationManager.notify(notification.id.hashCode(), builder.build())
        }
    }

    override fun supportsChannel(channel: NotificationChannel): Boolean =
        channel == NotificationChannel.PUSH || channel == NotificationChannel.IN_APP

    override fun getPriority(): NotificationPriority = NotificationPriority.HIGH
}