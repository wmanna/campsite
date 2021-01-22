package com.upgrade.campsite.service.notification;

import com.upgrade.campsite.dto.ReservationDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailNotificationService implements NotificationService {

    public static final String SUPPORTS_EMAIL_PREFERENCE = "user-email-notification";
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationService.class);

    @Override
    public void notifyUser(ReservationDto dto) {
        if (this.supports(dto.getUserPreferences())) {
            LOGGER.info("notifyUser... " + dto.getUserEmailAddress());
            try {
                Thread.sleep(5000L); // 5 seconds.
            } catch (InterruptedException iEx) {
                LOGGER.error("InterruptedException: notifyUser (email) with error.");
            }
            LOGGER.info("notifyUser DONE! Email sent successfully.");
        }
    }

    @Override
    public boolean supports(List<String> userPreferences) {
        return userPreferences.stream().anyMatch(s -> s.equals(SUPPORTS_EMAIL_PREFERENCE));
    }
}
