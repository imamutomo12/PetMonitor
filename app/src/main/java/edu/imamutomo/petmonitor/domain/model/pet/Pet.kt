package edu.imamutomo.petmonitor.domain.model.pet

data class Pet( val id: String,
                val name: String,
                val species: String,
                val breed: String?,
                val birthDate: Long?,
                val weight: Double?,
                val color: String?,
                val microchipId: String?,
                val ownerName: String,
                val ownerContact: String,
                val veterinarianContact: String?,
                val allergies: List<String>,
                val medicalNotes: String?,
                val photoUrl: String?,
                val createdAt: Long,
                val updatedAt: Long) {
    // Builder class as inner class for clean API
    class Builder(
        private val id: String,
        private val name: String,
        private val species: String,
        private val ownerName: String,
        private val ownerContact: String
    ) {
        private var breed: String? = null
        private var birthDate: Long? = null
        private var weight: Double? = null
        private var color: String? = null
        private var microchipId: String? = null
        private var veterinarianContact: String? = null
        private var allergies: MutableList<String> = mutableListOf()
        private var medicalNotes: String? = null
        private var photoUrl: String? = null
        private var createdAt: Long = System.currentTimeMillis()
        private var updatedAt: Long = System.currentTimeMillis()

        fun breed(breed: String) = apply { this.breed = breed }
        fun birthDate(date: Long?) = apply { this.birthDate = date }
        fun weight(weight: Double) = apply { this.weight = weight }
        fun color(color: String) = apply { this.color = color }
        fun microchipId(id: String) = apply { this.microchipId = id }
        fun veterinarianContact(contact: String) = apply { this.veterinarianContact = contact }
        fun addAllergy(allergy: String) = apply { this.allergies.add(allergy) }
        fun medicalNotes(notes: String) = apply { this.medicalNotes = notes }
        fun photoUrl(url: String) = apply { this.photoUrl = url }
        fun createdAt(timestamp: Long) = apply { this.createdAt = timestamp }
        fun updatedAt(timestamp: Long) = apply { this.updatedAt = timestamp }

        fun build(): Pet {
            return Pet(
                id = id,
                name = name,
                species = species,
                breed = breed,
                birthDate = birthDate,
                weight = weight,
                color = color,
                microchipId = microchipId,
                ownerName = ownerName,
                ownerContact = ownerContact,
                veterinarianContact = veterinarianContact,
                allergies = allergies.toList(),
                medicalNotes = medicalNotes,
                photoUrl = photoUrl,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    // Copy builder for updates
    fun toBuilder(): Builder {
        return Builder(id, name, species, ownerName, ownerContact)
            .breed(breed ?: "")
            .birthDate(birthDate)
            .weight(weight ?: 0.0)
            .color(color ?: "")
            .microchipId(microchipId ?: "")
            .veterinarianContact(veterinarianContact ?: "")
            .apply { allergies.forEach { addAllergy(it) } }
            .medicalNotes(medicalNotes ?: "")
            .photoUrl(photoUrl ?: "")
            .createdAt(createdAt)
            .updatedAt(System.currentTimeMillis())
    }
}