package edu.imamutomo.petmonitor.domain.export

import android.content.Context
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ExportManager @Inject constructor(
    private val context: Context
) {
    private val elements = mutableListOf<ExportableElement>()

    fun addPet(pet: Pet) {
        elements.add(PetExportWrapper(pet))
    }

    fun addReminder(reminder: ReminderType) {
        elements.add(ReminderExportWrapper(reminder))
    }

    fun clear() {
        elements.clear()
    }

    suspend fun export(visitor: DataExportVisitor): Result<ExportResult> = withContext(Dispatchers.IO) {
        runCatching {
            val content = buildString {
                append(visitor.getHeader())
                elements.forEachIndexed { index, element ->
                    if (index > 0) append(",\n")
                    append(element.accept(visitor))
                }
                append(visitor.getFooter())
            }

            val filename = "petcare_export_${System.currentTimeMillis()}.${visitor.getFileExtension()}"
            val file = File(context.cacheDir, filename)
            file.writeText(content)

            ExportResult(
                file = file,
                filename = filename,
                mimeType = visitor.getMimeType(),
                recordCount = elements.size
            )
        }
    }

    fun getAvailableVisitors(): List<DataExportVisitor> = listOf(
        JsonExportVisitor(),
        XmlExportVisitor(),
        CsvExportVisitor()
    )
}