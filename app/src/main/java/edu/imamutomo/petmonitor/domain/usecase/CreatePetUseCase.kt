package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.repository.PetRepository
import edu.imamutomo.petmonitor.domain.validation.PetValidationChain
import edu.imamutomo.petmonitor.domain.validation.PetValidationRequest
import edu.imamutomo.petmonitor.domain.validation.ValidationException
import edu.imamutomo.petmonitor.domain.validation.ValidationResult
import javax.inject.Inject

class CreatePetUseCase @Inject constructor(
    private val petRepository: PetRepository,
    private val validationChain: PetValidationChain
) {
    suspend operator fun invoke(builder: Pet.Builder): Result<Pet> {
        val pet = builder.build()

        val request = PetValidationRequest(
            name = pet.name,
            species = pet.species,
            ownerName = pet.ownerName,
            ownerContact = pet.ownerContact,
            birthDate = pet.birthDate,
            weight = pet.weight,
            microchipId = pet.microchipId
        )

        return when (val validation = validationChain.validate(request)) {
            is ValidationResult.Success -> {
                petRepository.savePet(pet)
            }
            is ValidationResult.Failure -> {
                Result.failure(ValidationException(validation.errors))
            }
        }
    }
}