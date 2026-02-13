package edu.imamutomo.petmonitor.data.repository

import edu.imamutomo.petmonitor.data.local.dao.PetDao
import edu.imamutomo.petmonitor.data.local.entity.PetEntity
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepositoryImpl @Inject constructor(
    private val petDao: PetDao
) : PetRepository {
    override fun getAllPets(): Flow<List<Pet>> {
        return petDao.getAllPets().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getPetById(petId: String): Pet? {
        return petDao.getPetById(petId)?.toDomainModel()
    }

    override suspend fun savePet(pet: Pet): Result<Pet> = runCatching {
        petDao.insertPet(pet.toEntity())
        pet
    }

    override suspend fun updatePet(pet: Pet): Result<Pet> = runCatching {
        petDao.updatePet(pet.toEntity())
        pet
    }

    override suspend fun deletePet(petId: String): Result<Unit> = runCatching {
        val pet = petDao.getPetById(petId) ?: throw IllegalArgumentException("Pet not found")
        petDao.deletePet(pet)
    }

    override fun getPetsBySpecies(species: String): Flow<List<Pet>> {
        return petDao.getPetsBySpecies(species).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    private fun PetEntity.toDomainModel(): Pet {
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
            allergies = allergies,
            medicalNotes = medicalNotes,
            photoUrl = photoUrl,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun Pet.toEntity(): PetEntity {
        return PetEntity(
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
            allergies = allergies,
            medicalNotes = medicalNotes,
            photoUrl = photoUrl,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}