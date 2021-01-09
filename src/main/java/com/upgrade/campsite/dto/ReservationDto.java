package com.upgrade.campsite.dto;

import java.time.LocalDate;

public class ReservationDto {

    private String reservationCode;
    private String reservationDate;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private String userEmailAddress;
    private String userFullName;
    private boolean cancellation;

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
}
