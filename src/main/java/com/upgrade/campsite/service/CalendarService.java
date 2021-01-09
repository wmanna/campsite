package com.upgrade.campsite.service;

import com.upgrade.campsite.dto.AvailableCalendar;
import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.ReservationCalendar;
import com.upgrade.campsite.repository.ReservationCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CalendarService {

    @Value( "${reservation.calendar.max}")
    private Integer maxDays;

    @Value( "${reservation.calendar.min}")
    private Integer minDays;

    @Autowired
    private ReservationCalendarRepository reservationCalendarRepository;

    public AvailableCalendar search(LocalDate startDate, LocalDate endDate) {

        Set<String> availableDates = new TreeSet<>();

        startDate = !Objects.isNull(startDate) ? startDate : LocalDate.now().plusDays(minDays);
        endDate = !Objects.isNull(endDate) ? endDate : LocalDate.now().plusDays(maxDays);

        // Set with next 30 days.
        LocalDate calendarDate = startDate;
        for (int i = 1; i <= maxDays; i++) {
            availableDates.add(calendarDate.toString());
            calendarDate = calendarDate.plusDays(1);
        }

        // Search unavailable dates.
        List<ReservationCalendar> calendar = reservationCalendarRepository.findByCalendarDateGreaterThanEqualAndCalendarDateLessThanEqual(startDate, endDate);

        // Remove unavailable dates from set.
        for (ReservationCalendar reservationDate : calendar) {
            availableDates.remove(reservationDate.getCalendarDate().format(DateTimeFormatter.ISO_DATE));
        }

        return new AvailableCalendar(availableDates, availableDates.size());
    }

    public void updateCalendar(ReservationDto reservationDto) {

        long totalDays = DAYS.between(reservationDto.getArrivalDate(), reservationDto.getDepartureDate());

        for (int i = 0; i < totalDays ; i++) {
            ReservationCalendar reservationCalendar = new ReservationCalendar();
            reservationCalendar.setCalendarDate(reservationDto.getArrivalDate().plusDays(i));
            reservationCalendar.setReservationCode(reservationDto.getReservationCode());
            reservationCalendarRepository.save(reservationCalendar);
        }
    }
}
