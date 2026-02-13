package edu.imamutomo.petmonitor.domain.model.notification

data class NotificationData(
    val id: String,
    val title: String,
    val message: String,
    val channel: NotificationChannel,
    val petId: String?,
    val reminderId: String?,
    val timestamp: Long,
    val actions: List<NotificationAction> = emptyList()
)
