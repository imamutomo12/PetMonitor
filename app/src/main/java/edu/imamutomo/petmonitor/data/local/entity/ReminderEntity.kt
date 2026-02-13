package edu.imamutomo.petmonitor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val description: String,
    val scheduledTime: Long,
    val petId: String,
    val parameters: Map<String, String>,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
