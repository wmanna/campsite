package com.upgrade.campsite.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_calendar")
public class ReservationCalendar {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",  strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "calendar_date")
    private LocalDate calendarDate;

    @Column(name = "reservation_code")
    private String reservationCode;

    public void setCalendarDate(LocalDate calendarDate) {
        this.calendarDate = calendarDate;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public LocalDate getCalendarDate() {
        return calendarDate;
    }
}
