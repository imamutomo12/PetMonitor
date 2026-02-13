package edu.imamutomo.petmonitor.domain.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Failure(val errors: List<String>) : ValidationResult()
}

abstract class AbstractValidationHandler : ValidationHandler {
    override var next: ValidationHandler? = null

    protected fun validateNext(request: PetValidationRequest): ValidationResult {
        return next?.handle(request) ?: ValidationResult.Success
    }
}