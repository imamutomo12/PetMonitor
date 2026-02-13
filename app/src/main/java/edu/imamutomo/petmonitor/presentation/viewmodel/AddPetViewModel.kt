package edu.imamutomo.petmonitor.presentation.viewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.usecase.CreatePetUseCase
import edu.imamutomo.petmonitor.domain.validation.ValidationException
import edu.imamutomo.petmonitor.presentation.state.AddPetUiState
import edu.imamutomo.petmonitor.presentation.state.AppStateManager

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val createPetUseCase: CreatePetUseCase,
    private val stateManager: AppStateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetUiState())
    val uiState: StateFlow<AddPetUiState> = _uiState.asStateFlow()

    private var currentBuilder: Pet.Builder? = null

    fun startBuilding(id: String, name: String, species: String, ownerName: String, ownerContact: String) {
        currentBuilder = Pet.Builder(id, name, species, ownerName, ownerContact)
        _uiState.update {
            it.copy(
                isValid = false,
                currentStep = 1,
                totalSteps = 4
            )
        }
    }

    fun setBasicInfo(breed: String?, birthDate: Long?, color: String?) {
        currentBuilder?.let { builder ->
            breed?.let { builder.breed(it) }
            birthDate?.let { builder.birthDate(it) }
            color?.let { builder.color(it) }
            _uiState.update { it.copy(currentStep = 2) }
        }
    }

    fun setMedicalInfo(weight: Double?, microchipId: String?, allergies: List<String>) {
        currentBuilder?.let { builder ->
            weight?.let { builder.weight(it) }
            microchipId?.let { builder.microchipId(it) }
            allergies.forEach { builder.addAllergy(it) }
            _uiState.update { it.copy(currentStep = 3) }
        }
    }

    fun setAdditionalInfo(vetContact: String?, medicalNotes: String?, photoUrl: String?) {
        currentBuilder?.let { builder ->
            vetContact?.let { builder.veterinarianContact(it) }
            medicalNotes?.let { builder.medicalNotes(it) }
            photoUrl?.let { builder.photoUrl(it) }
            _uiState.update { it.copy(currentStep = 4, isValid = true) }
        }
    }

    fun savePet() {
        viewModelScope.launch {
            currentBuilder?.let { builder ->
                _uiState.update { it.copy(isLoading = true) }

                createPetUseCase(builder)
                    .onSuccess { pet ->
                        stateManager.updateState(
                            { it.copy(selectedPetId = pet.id) },
                            "Created pet: ${pet.name}"
                        )
                        _uiState.update {
                            it.copy(isLoading = false, isSuccess = true, pet = pet)
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = (e as? ValidationException)?.errors?.joinToString("\n")
                                    ?: e.message
                            )
                        }
                    }
            }
        }
    }

    fun undo() {
        viewModelScope.launch {
            stateManager.undo()
        }
    }
}