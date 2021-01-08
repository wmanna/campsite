package com.upgrade.campsite.dto;

import java.time.LocalDate;

public class ReservationDto {

    private String reservationCode;
    private String reservationDate;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private String userEmailAddress;
    private String userFullName;

    public String getReservationCode() {
        return reservationCode;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
