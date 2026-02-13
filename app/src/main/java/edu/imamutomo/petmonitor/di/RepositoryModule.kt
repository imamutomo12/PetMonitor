package edu.imamutomo.petmonitor.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.imamutomo.petmonitor.data.repository.PetRepositoryImpl
import edu.imamutomo.petmonitor.data.repository.ReminderRepositoryImpl
import edu.imamutomo.petmonitor.domain.notification.EmailService
import edu.imamutomo.petmonitor.domain.notification.EmailServiceImpl
import edu.imamutomo.petmonitor.domain.notification.SMSService
import edu.imamutomo.petmonitor.domain.notification.SMSServiceImpl
import edu.imamutomo.petmonitor.domain.repository.PetRepository
import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPetRepository(
        impl: PetRepositoryImpl
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        impl: ReminderRepositoryImpl
    ): ReminderRepository

    @Binds
    @Singleton
    abstract fun bindEmailService(
        impl: EmailServiceImpl
    ): EmailService

    @Binds
    @Singleton
    abstract fun bindSMSService(
        impl: SMSServiceImpl
    ): SMSService
}