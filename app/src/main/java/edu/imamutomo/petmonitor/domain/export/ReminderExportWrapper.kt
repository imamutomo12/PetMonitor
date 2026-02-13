package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType

data class ReminderExportWrapper(val reminder: ReminderType) : ExportableElement {
    override fun accept(visitor: DataExportVisitor): String = visitor.visit(reminder)
}