package edu.imamutomo.petmonitor.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.imamutomo.petmonitor.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE petId = :petId ORDER BY scheduledTime ASC")
    fun getRemindersForPet(petId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE scheduledTime > :currentTime AND isCompleted = 0 ORDER BY scheduledTime ASC")
    fun getUpcomingReminders(currentTime: Long = System.currentTimeMillis()): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Query("UPDATE reminders SET isCompleted = 1 WHERE id = :reminderId")
    suspend fun markAsCompleted(reminderId: String)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE type = :type")
    fun getRemindersByType(type: String): Flow<List<ReminderEntity>>
}
