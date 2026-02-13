package edu.imamutomo.petmonitor.domain.repository

import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getRemindersForPet(petId: String): Flow<List<ReminderType>>
    fun getUpcomingReminders(): Flow<List<ReminderType>>
    suspend fun getReminderById(reminderId: String): ReminderType?
    suspend fun saveReminder(reminder: ReminderType): Result<ReminderType>
    suspend fun updateReminder(reminder: ReminderType): Result<ReminderType>
    suspend fun markAsCompleted(reminderId: String): Result<Unit>
    suspend fun deleteReminder(reminderId: String): Result<Unit>
}