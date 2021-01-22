package com.upgrade.campsite.service.notification;

import com.upgrade.campsite.dto.ReservationDto;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface NotificationService {
    
    String DEFAULT_NOTIFICATION_SERVICE = EmailNotificationService.SUPPORTS_EMAIL_PREFERENCE;

    @Async
    void notifyUser(ReservationDto dto);

    boolean supports(List<String> pref);

}

