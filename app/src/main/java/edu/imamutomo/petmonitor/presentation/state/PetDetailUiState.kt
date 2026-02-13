package edu.imamutomo.petmonitor.presentation.state

import edu.imamutomo.petmonitor.domain.export.ExportResult
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplate

data class PetDetailUiState(val isLoading: Boolean = false,
                            val pet: Pet? = null,
                            val reminders: List<ReminderType> = emptyList(),
                            val availableTemplates: List<ScheduleTemplate> = emptyList(),
                            val error: String? = null,
                            val exportResult: ExportResult? = null)
