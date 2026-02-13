package edu.imamutomo.petmonitor.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.imamutomo.petmonitor.domain.usecase.DeletePetUseCase
import edu.imamutomo.petmonitor.domain.usecase.GetAllPetsUseCase
import edu.imamutomo.petmonitor.presentation.state.AppStateManager
import edu.imamutomo.petmonitor.presentation.state.PetListUiState
import edu.imamutomo.petmonitor.presentation.state.Screen

@HiltViewModel
class PetListViewModel @Inject constructor(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val stateManager: AppStateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetListUiState())
    val uiState: StateFlow<PetListUiState> = _uiState.asStateFlow()

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            getAllPetsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
                .collect { pets ->
                    _uiState.update {
                        it.copy(isLoading = false, pets = pets, error = null)
                    }
                }
        }
    }

    fun selectPet(petId: String) {
        stateManager.updateState(
            { it.copy(selectedPetId = petId, currentScreen = Screen.PET_DETAIL) },
            "Selected pet: $petId"
        )
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            stateManager.createCheckpoint("Before delete pet: $petId")
            deletePetUseCase(petId)
                .onSuccess { loadPets() }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun filterBySpecies(species: String?) {
        _uiState.update { it.copy(filterSpecies = species) }
        // Implementation would filter the list
    }

    fun undo() {
        viewModelScope.launch {
            if (stateManager.undo()) {
                loadPets()
            }
        }
    }
}