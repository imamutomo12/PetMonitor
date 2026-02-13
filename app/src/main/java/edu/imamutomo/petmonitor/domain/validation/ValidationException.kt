package edu.imamutomo.petmonitor.domain.validation

class ValidationException(val errors: List<String>) : Exception(errors.joinToString(", "))
class NotFoundException(message: String) : Exception(message)