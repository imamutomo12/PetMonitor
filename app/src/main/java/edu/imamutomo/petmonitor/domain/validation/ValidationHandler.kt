package edu.imamutomo.petmonitor.domain.validation

interface ValidationHandler {
    var next: ValidationHandler?
    fun handle(request: PetValidationRequest): ValidationResult

    fun setNext(handler: ValidationHandler): ValidationHandler {
        this.next = handler
        return handler
    }
}