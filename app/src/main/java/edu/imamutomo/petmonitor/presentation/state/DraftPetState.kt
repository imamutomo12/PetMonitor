package edu.imamutomo.petmonitor.presentation.state

data class DraftPetState(
    val name: String,
    val species: String,
    val currentStep: Int,
    val completedFields: Set<String>
)
