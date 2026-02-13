package edu.imamutomo.petmonitor.domain.repository

import edu.imamutomo.petmonitor.domain.model.pet.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getAllPets(): Flow<List<Pet>>
    suspend fun getPetById(petId: String): Pet?
    suspend fun savePet(pet: Pet): Result<Pet>
    suspend fun updatePet(pet: Pet): Result<Pet>
    suspend fun deletePet(petId: String): Result<Unit>
    fun getPetsBySpecies(species: String): Flow<List<Pet>>
}