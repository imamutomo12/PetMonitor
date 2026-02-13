package edu.imamutomo.petmonitor.domain.validation

class ContactValidationHandler : AbstractValidationHandler() {
    override fun handle(request: PetValidationRequest): ValidationResult {
        val errors = mutableListOf<String>()

        if (request.ownerName.isBlank()) {
            errors.add("Owner name cannot be empty")
        }

        if (request.ownerContact.isBlank()) {
            errors.add("Owner contact cannot be empty")
        } else if (!isValidEmail(request.ownerContact) && !isValidPhone(request.ownerContact)) {
            errors.add("Owner contact must be valid email or phone number")
        }

        return if (errors.isEmpty()) {
            validateNext(request)
        } else {
            ValidationResult.Failure(errors)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }
}