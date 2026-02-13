package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.FoodReminder
import edu.imamutomo.petmonitor.domain.model.reminder.MedicineReminder
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.model.reminder.VaccineReminder
import org.json.JSONArray
import org.json.JSONObject

class JsonExportVisitor  : DataExportVisitor {
    override fun visit(pet: Pet): String {
        return JSONObject().apply {
            put("id", pet.id)
            put("name", pet.name)
            put("species", pet.species)
            put("breed", pet.breed ?: JSONObject.NULL)
            put("birthDate", pet.birthDate ?: JSONObject.NULL)
            put("weight", pet.weight ?: JSONObject.NULL)
            put("owner", JSONObject().apply {
                put("name", pet.ownerName)
                put("contact", pet.ownerContact)
            })
            put("allergies", JSONArray(pet.allergies))
            put("createdAt", pet.createdAt)
        }.toString(2)
    }

    override fun visit(reminder: ReminderType): String {
        return JSONObject().apply {
            put("id", reminder.id)
            put("type", reminder.javaClass.simpleName)
            put("title", reminder.title)
            put("description", reminder.description)
            put("scheduledTime", reminder.scheduledTime)
            put("petId", reminder.petId)

            when (reminder) {
                is FoodReminder -> {
                    put("foodType", reminder.foodType)
                    put("portionSize", reminder.portionSize)
                    put("calories", reminder.calories ?: JSONObject.NULL)
                }
                is MedicineReminder -> {
                    put("medicationName", reminder.medicationName)
                    put("dosage", reminder.dosage)
                    put("frequency", reminder.frequency.name)
                    put("withFood", reminder.withFood)
                }
                is VaccineReminder -> {
                    put("vaccineName", reminder.vaccineName)
                    put("veterinarian", reminder.veterinarian)
                    put("nextBoosterDate", reminder.nextBoosterDate ?: JSONObject.NULL)
                }
            }
        }.toString(2)
    }

    override fun getHeader(): String = "[\n"
    override fun getFooter(): String = "\n]"
    override fun getFileExtension(): String = "json"
    override fun getMimeType(): String = "application/json"
}