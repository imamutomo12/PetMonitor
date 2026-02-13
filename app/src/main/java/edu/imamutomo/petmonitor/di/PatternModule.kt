package edu.imamutomo.petmonitor.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.imamutomo.petmonitor.domain.export.ExportManager
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderFactoryProvider
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplateRegistry
import edu.imamutomo.petmonitor.domain.validation.PetValidationChain
import edu.imamutomo.petmonitor.presentation.state.StateHistoryManager
import edu.imamutomo.petmonitor.presentation.ui.components.PetTypeIconFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PatternModule {

    @Provides
    @Singleton
    fun providePetTypeIconFactory() = PetTypeIconFactory()

    @Provides
    @Singleton
    fun provideScheduleTemplateRegistry() = ScheduleTemplateRegistry()

    @Provides
    @Singleton
    fun provideStateHistoryManager() = StateHistoryManager()

    @Provides
    @Singleton
    fun providePetValidationChain() = PetValidationChain()

    @Provides
    @Singleton
    fun provideReminderFactoryProvider() = ReminderFactoryProvider()

    @Provides
    @Singleton
    fun provideExportManager(
        @ApplicationContext context: Context
    ) = ExportManager(context)
}