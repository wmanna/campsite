package com.upgrade.campsite.service.notification;

import com.upgrade.campsite.dto.ReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsNotificationService implements NotificationService {

    public static final String SUPPORTS_SMS_PREFERENCE = "user-sms-notification";
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationService.class);

    @Override
    public void notifyUser(ReservationDto dto) {
        if (this.supports(dto.getUserPreferences())) {
            LOGGER.info("notifyUser... ");
            try {
                Thread.sleep(10000L); // 10 seconds.
            } catch (InterruptedException iEx) {
                LOGGER.error("InterruptedException: notifyUser (SMS) with error.");
            }
            LOGGER.info("notifyUser DONE! SMS sent successfully.");
        }
    }

    @Override
    public boolean supports(List<String> userPreferences) {
        return userPreferences.stream().anyMatch(s -> s.equals(SUPPORTS_SMS_PREFERENCE));
    }
}
