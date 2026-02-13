package edu.imamutomo.petmonitor.domain.validation

class NameValidationHandler : AbstractValidationHandler() {
    override fun handle(request: PetValidationRequest): ValidationResult {
        val errors = mutableListOf<String>()

        if (request.name.isBlank()) {
            errors.add("Pet name cannot be empty")
        } else if (request.name.length < 2) {
            errors.add("Pet name must be at least 2 characters")
        } else if (!request.name.all { it.isLetter() || it.isWhitespace() }) {
            errors.add("Pet name can only contain letters and spaces")
        }

        return if (errors.isEmpty()) {
            validateNext(request)
        } else {
            ValidationResult.Failure(errors)
        }
    }
}