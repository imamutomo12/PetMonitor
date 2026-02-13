package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType

interface DataExportVisitor {
    fun visit(pet: Pet): String
    fun visit(reminder: ReminderType): String
    fun getHeader(): String
    fun getFooter(): String
    fun getFileExtension(): String
    fun getMimeType(): String
}