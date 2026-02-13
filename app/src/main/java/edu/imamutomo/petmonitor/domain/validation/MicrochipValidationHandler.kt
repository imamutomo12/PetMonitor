package edu.imamutomo.petmonitor.domain.validation

class MicrochipValidationHandler : AbstractValidationHandler() {
    override fun handle(request: PetValidationRequest): ValidationResult {
        request.microchipId?.let { id ->
            if (id.isNotBlank() && !id.matches(Regex("^[A-Z0-9]{9,15}$"))) {
                return ValidationResult.Failure(
                    listOf("Microchip ID must be 9-15 alphanumeric characters")
                )
            }
        }
        return validateNext(request)
    }
}