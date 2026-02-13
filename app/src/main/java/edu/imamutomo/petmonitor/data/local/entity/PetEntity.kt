package edu.imamutomo.petmonitor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String,
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
    val updatedAt: Long
)
