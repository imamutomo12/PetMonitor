package edu.imamutomo.petmonitor.domain.notification

interface EmailService {
    suspend fun sendEmail(subject: String, body: String, recipient: String): Result<Unit>
}
