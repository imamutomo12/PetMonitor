package edu.imamutomo.petmonitor.domain.notification

import javax.inject.Inject

class SMSServiceImpl @Inject constructor() : SMSService {
    override suspend fun sendSMS(phoneNumber: String, message: String): Result<Unit> {
        // Integration with SMS API (Twilio, etc.)
        return Result.success(Unit)
    }
}