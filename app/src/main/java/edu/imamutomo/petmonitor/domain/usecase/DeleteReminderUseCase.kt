package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import javax.inject.Inject


class DeleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminderId: String): Result<Unit> {
        return reminderRepository.deleteReminder(reminderId)
    }
}