package edu.imamutomo.petmonitor.domain.model.reminder

import javax.inject.Inject

sealed class ReminderType {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val scheduledTime: Long
    abstract val petId: String
}

data class FoodReminder(
    override val id: String,
    override val title: String,
    override val description: String,
    override val scheduledTime: Long,
    override val petId: String,
    val foodType: String,
    val portionSize: String,
    val calories: Int? = null
) : ReminderType()

data class MedicineReminder(
    override val id: String,
    override val title: String,
    override val description: String,
    override val scheduledTime: Long,
    override val petId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: MedicationFrequency,
    val withFood: Boolean
) : ReminderType()

data class VaccineReminder(
    override val id: String,
    override val title: String,
    override val description: String,
    override val scheduledTime: Long,
    override val petId: String,
    val vaccineName: String,
    val veterinarian: String,
    val nextBoosterDate: Long? = null
) : ReminderType()

enum class MedicationFrequency {
    ONCE_DAILY, TWICE_DAILY, THREE_TIMES_DAILY, WEEKLY, AS_NEEDED
}

// Abstract Factory Interface
interface ReminderFactory {
    fun createReminder(
        id: String,
        title: String,
        description: String,
        scheduledTime: Long,
        petId: String,
        additionalParams: Map<String, Any>
    ): ReminderType
}

// Concrete Factories
class FoodReminderFactory : ReminderFactory {
    override fun createReminder(
        id: String,
        title: String,
        description: String,
        scheduledTime: Long,
        petId: String,
        additionalParams: Map<String, Any>
    ): FoodReminder {
        return FoodReminder(
            id = id,
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            foodType = additionalParams["foodType"] as? String ?: "Dry Food",
            portionSize = additionalParams["portionSize"] as? String ?: "1 cup",
            calories = additionalParams["calories"] as? Int
        )
    }
}

class MedicineReminderFactory : ReminderFactory {
    override fun createReminder(
        id: String,
        title: String,
        description: String,
        scheduledTime: Long,
        petId: String,
        additionalParams: Map<String, Any>
    ): MedicineReminder {
        return MedicineReminder(
            id = id,
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            medicationName = additionalParams["medicationName"] as? String
                ?: throw IllegalArgumentException("Medication name required"),
            dosage = additionalParams["dosage"] as? String ?: "As prescribed",
            frequency = additionalParams["frequency"] as? MedicationFrequency
                ?: MedicationFrequency.ONCE_DAILY,
            withFood = additionalParams["withFood"] as? Boolean ?: false
        )
    }
}

class VaccineReminderFactory : ReminderFactory {
    override fun createReminder(
        id: String,
        title: String,
        description: String,
        scheduledTime: Long,
        petId: String,
        additionalParams: Map<String, Any>
    ): VaccineReminder {
        return VaccineReminder(
            id = id,
            title = title,
            description = description,
            scheduledTime = scheduledTime,
            petId = petId,
            vaccineName = additionalParams["vaccineName"] as? String
                ?: throw IllegalArgumentException("Vaccine name required"),
            veterinarian = additionalParams["veterinarian"] as? String ?: "",
            nextBoosterDate = additionalParams["nextBoosterDate"] as? Long
        )
    }
}

// Factory Provider for Dependency Injection
class ReminderFactoryProvider @Inject constructor() {
    fun getFactory(type: ReminderCategory): ReminderFactory {
        return when (type) {
            ReminderCategory.FOOD -> FoodReminderFactory()
            ReminderCategory.MEDICINE -> MedicineReminderFactory()
            ReminderCategory.VACCINE -> VaccineReminderFactory()
        }
    }
}

enum class ReminderCategory {
    FOOD, MEDICINE, VACCINE
}
