package edu.imamutomo.petmonitor.domain.validation

data class PetValidationRequest(
    val name: String,
    val species: String,
    val ownerName: String,
    val ownerContact: String,
    val birthDate: Long? = null,
    val weight: Double? = null,
    val microchipId: String? = null
)
