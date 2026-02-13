package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplate
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplateRegistry
import javax.inject.Inject

class GetScheduleTemplatesUseCase @Inject constructor(
    private val templateRegistry: ScheduleTemplateRegistry
) {
    operator fun invoke(petType: String? = null): List<ScheduleTemplate> {
        return petType?.let {
            templateRegistry.getTemplatesForPetType(it)
        } ?: templateRegistry.getAllTemplates()
    }
}