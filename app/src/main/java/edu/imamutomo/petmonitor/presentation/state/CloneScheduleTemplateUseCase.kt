package edu.imamutomo.petmonitor.presentation.state

import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplate
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplateRegistry
import edu.imamutomo.petmonitor.domain.model.schedule.TemplateCustomizations
import javax.inject.Inject

class CloneScheduleTemplateUseCase @Inject constructor(
    private val templateRegistry: ScheduleTemplateRegistry
) {
    operator fun invoke(
        templateId: String,
        customName: String? = null,
        customDescription: String? = null
    ): Result<ScheduleTemplate> {
        return templateRegistry.cloneTemplate(
            templateId = templateId,
            customizations = TemplateCustomizations(
                newName = customName,
                newDescription = customDescription
            )
        )
    }
}