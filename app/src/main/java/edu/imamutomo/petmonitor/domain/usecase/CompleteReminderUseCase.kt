package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import javax.inject.Inject

class CompleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminderId: String): Result<Unit> {
        return reminderRepository.markAsCompleted(reminderId)
    }
}