package edu.imamutomo.petmonitor.presentation.state

import edu.imamutomo.petmonitor.domain.model.pet.Pet

data class PetListUiState(val isLoading: Boolean = false,
                          val pets: List<Pet> = emptyList(),
                          val filterSpecies: String? = null,
                          val error: String? = null)
