package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.model.notification.NotificationChannel
import edu.imamutomo.petmonitor.domain.model.notification.NotificationData
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderCategory
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderFactoryProvider
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.notification.NotificationManager
import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import java.util.UUID
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val factoryProvider: ReminderFactoryProvider,
    private val notificationManager: NotificationManager
) {
    suspend operator fun invoke(
        category: ReminderCategory,
        petId: String,
        title: String,
        description: String,
        scheduledTime: Long,
        params: Map<String, Any>
    ): Result<ReminderType> = runCatching {
        val factory = factoryProvider.getFactory(category)
        val reminder = factory.createReminder(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            additionalParams = params
        )

        reminderRepository.saveReminder(reminder)

        // Schedule notification
        notificationManager.sendNotification(
            NotificationData(
                id = reminder.id,
                title = reminder.title,
                message = reminder.description,
                channel = NotificationChannel.PUSH,
                petId = petId,
                reminderId = reminder.id,
                timestamp = scheduledTime
            )
        )

        reminder
    }
}