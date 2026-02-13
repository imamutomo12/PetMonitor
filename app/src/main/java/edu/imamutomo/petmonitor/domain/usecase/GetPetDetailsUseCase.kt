package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.repository.PetRepository
import edu.imamutomo.petmonitor.domain.validation.NotFoundException
import javax.inject.Inject

class GetPetDetailsUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(petId: String): Result<Pet> {
        return petRepository.getPetById(petId)?.let {
            Result.success(it)
        } ?: Result.failure(NotFoundException("Pet not found"))
    }
}
