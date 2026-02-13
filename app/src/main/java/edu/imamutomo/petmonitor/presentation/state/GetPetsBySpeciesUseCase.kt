package edu.imamutomo.petmonitor.presentation.state

import edu.imamutomo.petmonitor.domain.repository.PetRepository
import javax.inject.Inject

class GetPetsBySpeciesUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    operator fun invoke(species: String) = petRepository.getPetsBySpecies(species)
}