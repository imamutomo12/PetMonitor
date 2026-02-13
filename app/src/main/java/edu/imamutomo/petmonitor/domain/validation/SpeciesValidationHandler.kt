package edu.imamutomo.petmonitor.domain.validation

class SpeciesValidationHandler : AbstractValidationHandler() {
    private val validSpecies = setOf("dog", "cat", "bird", "fish", "rabbit",
        "hamster", "reptile", "other")

    override fun handle(request: PetValidationRequest): ValidationResult {
        val errors = mutableListOf<String>()

        if (request.species.isBlank()) {
            errors.add("Species cannot be empty")
        } else if (!validSpecies.contains(request.species.lowercase())) {
            errors.add("Invalid species. Valid options: ${validSpecies.joinToString()}")
        }

        return if (errors.isEmpty()) {
            validateNext(request)
        } else {
            ValidationResult.Failure(errors)
        }
    }
}