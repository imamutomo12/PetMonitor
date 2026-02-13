package edu.imamutomo.petmonitor.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.imamutomo.petmonitor.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY createdAt DESC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: String): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("SELECT * FROM pets WHERE species = :species")
    fun getPetsBySpecies(species: String): Flow<List<PetEntity>>

    @Query("SELECT COUNT(*) FROM pets")
    suspend fun getPetCount(): Int
}