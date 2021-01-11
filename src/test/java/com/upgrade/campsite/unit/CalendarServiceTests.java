package com.upgrade.campsite.unit;

import com.upgrade.campsite.dto.AvailableCalendar;
import com.upgrade.campsite.entity.ReservationCalendar;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationCalendarRepository;
import com.upgrade.campsite.service.CalendarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CalendarServiceTests {

    @Mock
    private ReservationCalendarRepository reservationCalendarRepository;

    @InjectMocks
    private CalendarService calendarService;

    private final static int MAX_DAYS = 30;
    private List<ReservationCalendar> notAvailableDates;

    @BeforeEach
    public void init() {

        openMocks(this);

        ReflectionTestUtils.setField(calendarService, "minDays", 1);
        ReflectionTestUtils.setField(calendarService, "maxDays", MAX_DAYS);

        notAvailableDates = new ArrayList<>();
        ReservationCalendar reservationCalendar = new ReservationCalendar();
        reservationCalendar.setCalendarDate(LocalDate.now().plusDays(3));
        notAvailableDates.add(reservationCalendar);

    }

    @Test
    void searchAvailableDaysInCalendar() throws ApiErrorException {

        AvailableCalendar availableCalendar = calendarService.search(null, null);

        Assertions.assertEquals(MAX_DAYS, availableCalendar.getAvailableDates().size());

    }

    @Test
    void searchAvailableDaysInCalendarWhenOneDayIsNotAvailable() throws ApiErrorException {

        when(reservationCalendarRepository
                .findByCalendarDateGreaterThanEqualAndCalendarDateLessThanEqual(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(notAvailableDates);

        AvailableCalendar availableCalendar = calendarService.search(null, null);

        Assertions.assertEquals(MAX_DAYS - 1, availableCalendar.getAvailableDates().size());

    }

}
