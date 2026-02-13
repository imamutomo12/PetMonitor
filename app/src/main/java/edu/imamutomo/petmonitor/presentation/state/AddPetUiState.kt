package edu.imamutomo.petmonitor.presentation.state

import edu.imamutomo.petmonitor.domain.model.pet.Pet

data class AddPetUiState(val isLoading: Boolean = false,
                         val isValid: Boolean = false,
                         val isSuccess: Boolean = false,
                         val currentStep: Int = 1,
                         val totalSteps: Int = 4,
                         val pet: Pet? = null,
                         val error: String? = null)
