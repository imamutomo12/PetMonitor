package edu.imamutomo.petmonitor.domain.validation

import javax.inject.Inject

class PetValidationChain @Inject constructor() {
    // Build chain: Name -> Species -> Contact -> BirthDate -> Weight -> Microchip
    private val head: ValidationHandler = NameValidationHandler()

    init {
        head.setNext(SpeciesValidationHandler())
            .setNext(ContactValidationHandler())
            .setNext(BirthDateValidationHandler())
            .setNext(WeightValidationHandler())
            .setNext(MicrochipValidationHandler())
    }

    fun validate(request: PetValidationRequest): ValidationResult {
        return head.handle(request)
    }
}