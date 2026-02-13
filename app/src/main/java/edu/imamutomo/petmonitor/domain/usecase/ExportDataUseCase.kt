package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.export.CsvExportVisitor
import edu.imamutomo.petmonitor.domain.export.ExportFormat
import edu.imamutomo.petmonitor.domain.export.ExportManager
import edu.imamutomo.petmonitor.domain.export.ExportResult
import edu.imamutomo.petmonitor.domain.export.JsonExportVisitor
import edu.imamutomo.petmonitor.domain.export.XmlExportVisitor
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import javax.inject.Inject

class ExportDataUseCase @Inject constructor(
    private val exportManager: ExportManager
) {
    fun addPetToExport(pet: Pet) {
        exportManager.addPet(pet)
    }

    fun addReminderToExport(reminder: ReminderType) {
        exportManager.addReminder(reminder)
    }

    suspend fun export(format: ExportFormat): Result<ExportResult> {
        val visitor = when (format) {
            ExportFormat.JSON -> JsonExportVisitor()
            ExportFormat.XML -> XmlExportVisitor()
            ExportFormat.CSV -> CsvExportVisitor()
        }
        return exportManager.export(visitor)
    }

    fun clearExportQueue() {
        exportManager.clear()
    }

    fun getAvailableFormats(): List<ExportFormat> = ExportFormat.values().toList()
}