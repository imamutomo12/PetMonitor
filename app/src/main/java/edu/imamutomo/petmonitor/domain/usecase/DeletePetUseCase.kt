package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.repository.PetRepository
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(petId: String): Result<Unit> {
        return petRepository.deletePet(petId)
    }
}