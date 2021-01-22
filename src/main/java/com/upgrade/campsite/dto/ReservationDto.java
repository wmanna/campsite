package com.upgrade.campsite.dto;

import com.upgrade.campsite.service.notification.NotificationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReservationDto {

    private String reservationCode;
    private String reservationDate;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private String userEmailAddress;
    private String userFullName;
    private List<String> userPreferences;
    private boolean cancellation;

    public ReservationDto() {
        // Default preferences
        userPreferences = new ArrayList<>(
                Collections.singletonList(NotificationService.DEFAULT_NOTIFICATION_SERVICE));
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public boolean isCancellation() {
        return cancellation;
    }

    public void setCancellation(boolean cancellation) {
        this.cancellation = cancellation;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public List<String> getUserPreferences() {
        return userPreferences;
    }
}
