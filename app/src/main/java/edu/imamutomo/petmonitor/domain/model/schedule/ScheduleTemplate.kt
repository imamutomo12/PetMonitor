package edu.imamutomo.petmonitor.domain.model.schedule

import java.util.UUID
import javax.inject.Inject

interface Cloneable<T> {
    fun clone(): T
}
data class ScheduleTemplate( val id: String,
                             val name: String,
                             val description: String,
                             val items: List<ScheduleItem>,
                             val petType: String?, // null means applicable to all
                             val isSystemTemplate: Boolean
) : Cloneable<ScheduleTemplate> {

    override fun clone(): ScheduleTemplate {
        // Deep copy of items
        val clonedItems = items.map { it.copy(id = UUID.randomUUID().toString()) }
        return this.copy(
            id = UUID.randomUUID().toString(),
            items = clonedItems,
            isSystemTemplate = false // Clones are user templates
        )
    }

    // Create customized clone with modifications
    fun cloneWithModifications(
        newName: String? = null,
        newDescription: String? = null,
        itemModifier: ((ScheduleItem) -> ScheduleItem)? = null
    ): ScheduleTemplate {
        val modifiedItems = itemModifier?.let { modifier ->
            items.map { modifier(it.copy(id = UUID.randomUUID().toString())) }
        } ?: items.map { it.copy(id = UUID.randomUUID().toString()) }

        return this.copy(
            id = UUID.randomUUID().toString(),
            name = newName ?: "$name (Copy)",
            description = newDescription ?: description,
            items = modifiedItems,
            isSystemTemplate = false
        )
    }
}

data class ScheduleItem(
    val id: String,
    val reminderType: String,
    val title: String,
    val timeOffsetDays: Int,
    val timeOfDay: String, // "HH:mm" format
    val parameters: Map<String, String>
)

// Template Registry managing prototypes
class ScheduleTemplateRegistry @Inject constructor() {
    private val templates = mutableMapOf<String, ScheduleTemplate>()

    init {
        // Register system templates (prototypes)
        registerDefaultTemplates()
    }

    private fun registerDefaultTemplates() {
        // Puppy schedule template
        registerTemplate(
            ScheduleTemplate(
                id = "template_puppy",
                name = "Puppy Care Schedule",
                description = "Complete care schedule for puppies 0-6 months",
                items = listOf(
                    ScheduleItem(
                        id = "item1",
                        reminderType = "FOOD",
                        title = "Morning Meal",
                        timeOffsetDays = 0,
                        timeOfDay = "08:00",
                        parameters = mapOf("foodType" to "Puppy Food", "portionSize" to "1/2 cup")
                    ),
                    ScheduleItem(
                        id = "item2",
                        reminderType = "VACCINE",
                        title = "DHPP Vaccine",
                        timeOffsetDays = 42,
                        timeOfDay = "10:00",
                        parameters = mapOf("vaccineName" to "DHPP", "veterinarian" to "TBD")
                    )
                ),
                petType = "Dog",
                isSystemTemplate = true
            )
        )

        // Kitten schedule template
        registerTemplate(
            ScheduleTemplate(
                id = "template_kitten",
                name = "Kitten Care Schedule",
                description = "Complete care schedule for kittens 0-6 months",
                items = listOf(
                    ScheduleItem(
                        id = "item1",
                        reminderType = "FOOD",
                        title = "Wet Food Meal",
                        timeOffsetDays = 0,
                        timeOfDay = "08:00",
                        parameters = mapOf("foodType" to "Kitten Wet Food", "portionSize" to "1/4 can")
                    )
                ),
                petType = "Cat",
                isSystemTemplate = true
            )
        )
    }

    fun registerTemplate(template: ScheduleTemplate) {
        templates[template.id] = template
    }

    fun getTemplate(id: String): ScheduleTemplate? {
        return templates[id]
    }

    fun getAllTemplates(): List<ScheduleTemplate> = templates.values.toList()

    fun getTemplatesForPetType(petType: String): List<ScheduleTemplate> {
        return templates.values.filter {
            it.petType == null || it.petType.equals(petType, ignoreCase = true)
        }
    }

    // Clone template by ID
    fun cloneTemplate(templateId: String, customizations: TemplateCustomizations? = null): Result<ScheduleTemplate> {
        val template = templates[templateId]
            ?: return Result.failure(IllegalArgumentException("Template not found"))

        return Result.success(
            customizations?.let {
                template.cloneWithModifications(
                    newName = it.newName,
                    newDescription = it.newDescription,
                    itemModifier = it.itemModifier
                )
            } ?: template.clone()
        )
    }
}

data class TemplateCustomizations(
    val newName: String? = null,
    val newDescription: String? = null,
    val itemModifier: ((ScheduleItem) -> ScheduleItem)? = null
)
