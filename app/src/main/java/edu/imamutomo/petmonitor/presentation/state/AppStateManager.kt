package edu.imamutomo.petmonitor.presentation.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppStateManager @Inject constructor(
    private val historyManager: StateHistoryManager
) {
    private val _currentState = MutableStateFlow(
        AppState(
            currentScreen = Screen.DASHBOARD,
            selectedPetId = null,
            activeReminders = emptyList(),
            filterSettings = FilterSettings(null, true),
            sortOrder = SortOrder.DATE_ADDED,
            isEditing = false,
            draftPet = null
        )
    )
    val currentState: StateFlow<AppState> = _currentState.asStateFlow()

    fun updateState(transform: (AppState) -> AppState, description: String = "Update") {
        val newState = transform(_currentState.value)
        historyManager.saveState(_currentState.value, description)
        _currentState.value = newState
    }

    fun undo(): Boolean {
        val memento = historyManager.undo() ?: return false
        _currentState.value = memento.getState()
        return true
    }

    fun redo(): Boolean {
        val memento = historyManager.redo() ?: return false
        _currentState.value = memento.getState()
        return true
    }

    fun createCheckpoint(description: String) {
        historyManager.saveState(_currentState.value, description)
    }

    fun restoreCheckpoint(memento: Memento) {
        _currentState.value = memento.getState()
    }
}