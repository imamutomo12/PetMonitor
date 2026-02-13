package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType

class XmlExportVisitor : DataExportVisitor {
    override fun visit(pet: Pet): String {
        return buildString {
            appendLine("  <pet>")
            appendLine("    <id>${escapeXml(pet.id)}</id>")
            appendLine("    <name>${escapeXml(pet.name)}</name>")
            appendLine("    <species>${escapeXml(pet.species)}</species>")
            pet.breed?.let { appendLine("    <breed>${escapeXml(it)}</breed>") }
            appendLine("    <owner>")
            appendLine("      <name>${escapeXml(pet.ownerName)}</name>")
            appendLine("      <contact>${escapeXml(pet.ownerContact)}</contact>")
            appendLine("    </owner>")
            appendLine("  </pet>")
        }
    }

    override fun visit(reminder: ReminderType): String {
        return buildString {
            appendLine("  <reminder type=\"${reminder.javaClass.simpleName}\">")
            appendLine("    <id>${escapeXml(reminder.id)}</id>")
            appendLine("    <title>${escapeXml(reminder.title)}</title>")
            appendLine("    <scheduledTime>${reminder.scheduledTime}</scheduledTime>")
            appendLine("    <petId>${escapeXml(reminder.petId)}</petId>")
            appendLine("  </reminder>")
        }
    }

    override fun getHeader(): String = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<export>\n"
    override fun getFooter(): String = "</export>"
    override fun getFileExtension(): String = "xml"
    override fun getMimeType(): String = "application/xml"

    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}