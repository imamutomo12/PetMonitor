package edu.imamutomo.petmonitor.domain.usecase

import edu.imamutomo.petmonitor.domain.repository.ReminderRepository
import javax.inject.Inject

class GetRemindersForPetUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(petId: String) = reminderRepository.getRemindersForPet(petId)
}