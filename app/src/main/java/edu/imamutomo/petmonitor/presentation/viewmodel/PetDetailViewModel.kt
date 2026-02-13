package edu.imamutomo.petmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.imamutomo.petmonitor.domain.export.CsvExportVisitor
import edu.imamutomo.petmonitor.domain.export.ExportFormat
import edu.imamutomo.petmonitor.domain.export.ExportManager
import edu.imamutomo.petmonitor.domain.export.JsonExportVisitor
import edu.imamutomo.petmonitor.domain.export.XmlExportVisitor
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderCategory
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplateRegistry
import edu.imamutomo.petmonitor.domain.usecase.CompleteReminderUseCase
import edu.imamutomo.petmonitor.domain.usecase.CreatePetUseCase
import edu.imamutomo.petmonitor.domain.usecase.DeleteReminderUseCase
import edu.imamutomo.petmonitor.domain.usecase.ExportDataUseCase
import edu.imamutomo.petmonitor.domain.usecase.GetPetDetailsUseCase
import edu.imamutomo.petmonitor.domain.usecase.GetRemindersForPetUseCase
import edu.imamutomo.petmonitor.domain.usecase.GetScheduleTemplatesUseCase
import edu.imamutomo.petmonitor.domain.usecase.ScheduleReminderUseCase
import edu.imamutomo.petmonitor.domain.usecase.UpdatePetUseCase
import edu.imamutomo.petmonitor.presentation.state.AppStateManager
import edu.imamutomo.petmonitor.presentation.state.CloneScheduleTemplateUseCase
import edu.imamutomo.petmonitor.presentation.state.PetDetailUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel  @Inject constructor(
    private val createPetUseCase: CreatePetUseCase,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val exportManager: ExportManager,
    private val stateManager: AppStateManager,
    private val templateRegistry: ScheduleTemplateRegistry,
    private val getPetDetailsUseCase: GetPetDetailsUseCase,
    private val getRemindersForPetUseCase: GetRemindersForPetUseCase,
    private val getScheduleTemplatesUseCase: GetScheduleTemplatesUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val cloneScheduleTemplateUseCase: CloneScheduleTemplateUseCase,
    private val completeReminderUseCase: CompleteReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val exportDataUseCase: ExportDataUseCase,


) : ViewModel() {
    private val _uiState: MutableStateFlow<PetDetailUiState> = MutableStateFlow(PetDetailUiState())
    val uiState: StateFlow<PetDetailUiState> = _uiState.asStateFlow()

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getPetDetailsUseCase(petId)
                .onSuccess { pet ->
                    _uiState.update {
                        it.copy(isLoading = false, pet = pet, error = null)
                    }
                    loadReminders(petId)
                    loadTemplates(pet.species)
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }
    }

    private fun loadReminders(petId: String) {
        viewModelScope.launch {
            getRemindersForPetUseCase(petId)
                .collect { reminders ->
                    _uiState.update { it.copy(reminders = reminders) }
                }
        }
    }

    private fun loadTemplates(species: String) {
        val templates = getScheduleTemplatesUseCase(species)
        _uiState.update { it.copy(availableTemplates = templates) }
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            stateManager.createCheckpoint("Before updating pet: ${pet.name}")
            updatePetUseCase(pet)
                .onSuccess { updatedPet ->
                    _uiState.update { it.copy(pet = updatedPet) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun scheduleReminder(
        category: ReminderCategory,
        title: String,
        description: String,
        scheduledTime: Long,
        params: Map<String, Any>
    ) {
        viewModelScope.launch {
            val petId = _uiState.value.pet?.id ?: return@launch

            stateManager.createCheckpoint("Before scheduling reminder: $title")

            scheduleReminderUseCase(
                category = category,
                petId = petId,
                title = title,
                description = description,
                scheduledTime = scheduledTime,
                params = params
            ).onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun applyTemplate(templateId: String) {
        viewModelScope.launch {
            val petId = _uiState.value.pet?.id ?: return@launch

            cloneScheduleTemplateUseCase(templateId)
                .onSuccess { template ->
                    // Convert template items to reminders
                    template.items.forEach { item ->
                        val category = try {
                            ReminderCategory.valueOf(item.reminderType)
                        } catch (e: IllegalArgumentException) {
                            return@forEach
                        }

                        val scheduledTime = calculateTimeFromTemplate(item.timeOffsetDays, item.timeOfDay)

                        scheduleReminderUseCase(
                            category = category,
                            petId = petId,
                            title = item.title,
                            description = "From template: ${template.name}",
                            scheduledTime = scheduledTime,
                            params = item.parameters
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun completeReminder(reminderId: String) {
        viewModelScope.launch {
            completeReminderUseCase(reminderId)
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            stateManager.createCheckpoint("Before deleting reminder: $reminderId")
            deleteReminderUseCase(reminderId)
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun exportPetData(format: ExportFormat) {
        viewModelScope.launch {
            _uiState.value.pet?.let { pet ->
                exportDataUseCase.clearExportQueue()
                exportDataUseCase.addPetToExport(pet)
                _uiState.value.reminders.forEach { reminder ->
                    exportDataUseCase.addReminderToExport(reminder)
                }

                exportDataUseCase.export(format)
                    .onSuccess { result ->
                        _uiState.update { it.copy(exportResult = result) }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(error = e.message) }
                    }
            }
        }
    }

    fun undo() {
        viewModelScope.launch {
            if (stateManager.undo()) {
                _uiState.value.pet?.id?.let { loadPetDetails(it) }
            }
        }
    }

    fun redo() {
        viewModelScope.launch {
            if (stateManager.redo()) {
                _uiState.value.pet?.id?.let { loadPetDetails(it) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun calculateTimeFromTemplate(daysOffset: Int, timeOfDay: String): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, daysOffset)

        val (hour, minute) = timeOfDay.split(":").map { it.toInt() }
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)

        return calendar.timeInMillis
    }
}