package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPetsUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    operator fun invoke(): Flow<List<Pet>> = petRepository.getAllPets()
}
