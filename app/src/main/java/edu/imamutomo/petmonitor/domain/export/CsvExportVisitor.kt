package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.FoodReminder
import edu.imamutomo.petmonitor.domain.model.reminder.MedicineReminder
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.model.reminder.VaccineReminder

class CsvExportVisitor : DataExportVisitor {
    private var isFirstRow = true

    override fun visit(pet: Pet): String {
        return if (isFirstRow) {
            isFirstRow = false
            "ID,Name,Species,Breed,Owner,Contact,CreatedAt\n" +
                    "${pet.id},${escapeCsv(pet.name)},${pet.species},${escapeCsv(pet.breed ?: "")}," +
                    "${escapeCsv(pet.ownerName)},${escapeCsv(pet.ownerContact)},${pet.createdAt}"
        } else {
            "${pet.id},${escapeCsv(pet.name)},${pet.species},${escapeCsv(pet.breed ?: "")}," +
                    "${escapeCsv(pet.ownerName)},${escapeCsv(pet.ownerContact)},${pet.createdAt}"
        }
    }

    override fun visit(reminder: ReminderType): String {
        val base = "${reminder.id},${escapeCsv(reminder.title)},${reminder.scheduledTime},${reminder.petId}"
        return when (reminder) {
            is FoodReminder -> "FOOD,$base,${escapeCsv(reminder.foodType)},${escapeCsv(reminder.portionSize)},"
            is MedicineReminder -> "MEDICINE,$base,${escapeCsv(reminder.medicationName)},${escapeCsv(reminder.dosage)},${reminder.frequency}"
            is VaccineReminder -> "VACCINE,$base,${escapeCsv(reminder.vaccineName)},${escapeCsv(reminder.veterinarian)},"
        }
    }

    override fun getHeader(): String = "Type,ID,Title,ScheduledTime,PetId,Field1,Field2,Field3\n"
    override fun getFooter(): String = ""
    override fun getFileExtension(): String = "csv"
    override fun getMimeType(): String = "text/csv"

    private fun escapeCsv(text: String): String {
        return if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            "\"${text.replace("\"", "\"\"")}\""
        } else {
            text
        }
    }
}