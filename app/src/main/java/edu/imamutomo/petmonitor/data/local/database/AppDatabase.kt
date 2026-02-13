package edu.imamutomo.petmonitor.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.imamutomo.petmonitor.data.local.converter.Converters
import edu.imamutomo.petmonitor.data.local.dao.PetDao
import edu.imamutomo.petmonitor.data.local.dao.ReminderDao
import edu.imamutomo.petmonitor.data.local.entity.PetEntity
import edu.imamutomo.petmonitor.data.local.entity.ReminderEntity

@Database(
    entities = [PetEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        const val DATABASE_NAME = "petcare_database"
    }
}