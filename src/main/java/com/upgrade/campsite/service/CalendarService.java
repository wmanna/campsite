package com.upgrade.campsite.service;

import com.upgrade.campsite.dto.AvailableCalendarDto;
import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.ReservationCalendar;
import com.upgrade.campsite.exception.ApiErrorException;
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

    public AvailableCalendarDto search(LocalDate startDate, LocalDate endDate) {

        startDate = !Objects.isNull(startDate) ? startDate : LocalDate.now().plusDays(minDays);
        endDate = !Objects.isNull(endDate) ? endDate : LocalDate.now().plusDays(maxDays);

        if (DAYS.between(startDate, endDate) > maxDays) {
            endDate = LocalDate.now().plusDays(maxDays);
        }

        Set<String> availableDates = getCalendarDaysInARange(startDate, endDate);

        // Search unavailable dates.
        List<ReservationCalendar> calendar = reservationCalendarRepository
                .findByCalendarDateGreaterThanEqualAndCalendarDateLessThanEqual(startDate, endDate);

        // Remove unavailable dates from set.
        for (ReservationCalendar reservationDate : calendar) {
            availableDates.remove(reservationDate.getCalendarDate().format(DateTimeFormatter.ISO_DATE));
        }

        return new AvailableCalendarDto(availableDates, availableDates.size());
    }

    public void updateCalendar(ReservationDto dto) {

        if (dto.isCancellation()) {
            reservationCalendarRepository.deleteByReservationCode(dto.getReservationCode());
            return;
        }

        long totalDays = DAYS.between(dto.getArrivalDate(), dto.getDepartureDate());

        for (int i = 0; i < totalDays ; i++) {
            ReservationCalendar reservationCalendar = new ReservationCalendar();
            reservationCalendar.setCalendarDate(dto.getArrivalDate().plusDays(i));
            reservationCalendar.setReservationCode(dto.getReservationCode());
            reservationCalendarRepository.save(reservationCalendar);
        }
    }

    private Set<String> getCalendarDaysInARange(LocalDate startDate, LocalDate endDate) {

        Set<String> calendarDays = new TreeSet<>();
        LocalDate auxLocalDate = startDate;

        for (int i = 1; i <= DAYS.between(startDate, endDate) + 1; i++) {
            calendarDays.add(auxLocalDate.toString());
            auxLocalDate = auxLocalDate.plusDays(1);
        }

        return calendarDays;
    }

}
