package edu.imamutomo.petmonitor.domain.notification

interface SMSService {
    suspend fun sendSMS(phoneNumber: String, message: String): Result<Unit>
}