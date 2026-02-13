package edu.imamutomo.petmonitor.data.repository

import edu.imamutomo.petmonitor.data.local.dao.ReminderDao
import edu.imamutomo.petmonitor.data.local.entity.ReminderEntity
import edu.imamutomo.petmonitor.domain.model.reminder.FoodReminder
import edu.imamutomo.petmonitor.domain.model.reminder.MedicineReminder
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderCategory
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderFactoryProvider
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.model.reminder.VaccineReminder
import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val factoryProvider: ReminderFactoryProvider
) : ReminderRepository {

    override fun getRemindersForPet(petId: String): Flow<List<ReminderType>> {
        return reminderDao.getRemindersForPet(petId).map { entities ->
            entities.mapNotNull { it.toDomainModel() }
        }
    }

    override fun getUpcomingReminders(): Flow<List<ReminderType>> {
        return reminderDao.getUpcomingReminders().map { entities ->
            entities.mapNotNull { it.toDomainModel() }
        }
    }

    override suspend fun getReminderById(reminderId: String): ReminderType? {
        return reminderDao.getReminderById(reminderId)?.toDomainModel()
    }

    override suspend fun saveReminder(reminder: ReminderType): Result<ReminderType> = runCatching {
        reminderDao.insertReminder(reminder.toEntity())
        reminder
    }

    override suspend fun updateReminder(reminder: ReminderType): Result<ReminderType> = runCatching {
        reminderDao.updateReminder(reminder.toEntity())
        reminder
    }

    override suspend fun markAsCompleted(reminderId: String): Result<Unit> = runCatching {
        reminderDao.markAsCompleted(reminderId)
    }

    override suspend fun deleteReminder(reminderId: String): Result<Unit> = runCatching {
        val reminder = reminderDao.getReminderById(reminderId)
            ?: throw IllegalArgumentException("Reminder not found")
        reminderDao.deleteReminder(reminder)
    }

    private fun ReminderEntity.toDomainModel(): ReminderType? {
        val category = try {
            ReminderCategory.valueOf(type)
        } catch (e: IllegalArgumentException) {
            return null
        }

        val factory = factoryProvider.getFactory(category)
        val params = parameters.mapValues { (_, value) ->
            when {
                value.toIntOrNull() != null -> value.toInt()
                value.toBooleanStrictOrNull() != null -> value.toBoolean()
                else -> value
            }
        }

        return factory.createReminder(
            id = id,
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            additionalParams = params
        )
    }

    private fun ReminderType.toEntity(): ReminderEntity {
        val (type, params) = when (this) {
            is FoodReminder -> "FOOD" to mapOf(
                "foodType" to foodType,
                "portionSize" to portionSize,
                "calories" to (calories?.toString() ?: "")
            )
            is MedicineReminder -> "MEDICINE" to mapOf(
                "medicationName" to medicationName,
                "dosage" to dosage,
                "frequency" to frequency.name,
                "withFood" to withFood.toString()
            )
            is VaccineReminder -> "VACCINE" to mapOf(
                "vaccineName" to vaccineName,
                "veterinarian" to veterinarian,
                "nextBoosterDate" to (nextBoosterDate?.toString() ?: "")
            )
            else -> throw IllegalArgumentException("Unknown reminder type")
        }

        return ReminderEntity(
            id = id,
            type = type,
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            parameters = params,
            isCompleted = false,
            createdAt = System.currentTimeMillis()
        )
    }
}