package edu.imamutomo.petmonitor.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.imamutomo.petmonitor.domain.notification.EmailNotificationSender
import edu.imamutomo.petmonitor.domain.notification.EmailService
import edu.imamutomo.petmonitor.domain.notification.NotificationSender
import edu.imamutomo.petmonitor.domain.notification.PushNotificationSender
import edu.imamutomo.petmonitor.domain.notification.SMSNotificationSender
import edu.imamutomo.petmonitor.domain.notification.SMSService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationSenders(
        @ApplicationContext context: Context,
        emailService: EmailService,
        smsService: SMSService
    ): Set<NotificationSender> {
        return setOf(
            PushNotificationSender(context, NotificationManagerCompat.from(context)),
            EmailNotificationSender(emailService),
            SMSNotificationSender(smsService)
        )
    }
}
