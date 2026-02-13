package edu.imamutomo.petmonitor.domain.validation

class BirthDateValidationHandler : AbstractValidationHandler() {
    override fun handle(request: PetValidationRequest): ValidationResult {
        request.birthDate?.let { date ->
            if (date > System.currentTimeMillis()) {
                return ValidationResult.Failure(listOf("Birth date cannot be in the future"))
            }
            if (date < System.currentTimeMillis() - (30 * 365 * 24 * 60 * 60 * 1000)) {
                return ValidationResult.Failure(listOf("Pet age seems unrealistic (>30 years)"))
            }
        }
        return validateNext(request)
    }
}

class WeightValidationHandler : AbstractValidationHandler() {
    override fun handle(request: PetValidationRequest): ValidationResult {
        request.weight?.let { weight ->
            if (weight <= 0) {
                return ValidationResult.Failure(listOf("Weight must be positive"))
            }
            if (weight > 150) { // 150kg max for largest pets
                return ValidationResult.Failure(listOf("Weight seems unrealistic (>150kg)"))
            }
        }
        return validateNext(request)
    }
}