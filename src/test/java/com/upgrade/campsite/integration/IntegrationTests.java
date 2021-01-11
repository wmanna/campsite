package com.upgrade.campsite.integration;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationRepository;
import com.upgrade.campsite.service.CalendarService;
import com.upgrade.campsite.service.ReservationService;
import com.upgrade.campsite.service.ResourceLockService;
import com.upgrade.campsite.service.UserService;
import com.upgrade.campsite.validator.DatesValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@DataJpaTest
public class IntegrationTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @Mock
    private UserService userService;

    @Mock
    private ResourceLockService resourceLockService;

    @Mock
    private CalendarService calendarService;

    @Mock
    private DatesValidator datesValidator;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationDto reservationDto;

    @BeforeEach
    public void init() {

        openMocks(this);

        ReflectionTestUtils.setField(datesValidator, "minDays", 1);
        ReflectionTestUtils.setField(datesValidator, "maxDays", 30);
        ReflectionTestUtils.setField(reservationService, "reservationRepository", reservationRepository);

        // Reservation DTO
        reservationDto = new ReservationDto();
        reservationDto.setArrivalDate(LocalDate.now().plusDays(2));
        reservationDto.setDepartureDate(LocalDate.now().plusDays(4));
        reservationDto.setUserEmailAddress("test@email.com");
        reservationDto.setUserFullName("Test T. Test");

    }

    @Test
    void createNewReservationIntegrationTest() throws ApiErrorException {

        // 1) Create new reservation.
        String reservationCode = reservationService.createOrModifyReservation(reservationDto).getCode();

        Assertions.assertNotNull(reservationCode);

        // 2) Get created reservation.
        Reservation reservation = reservationService.getReservationByCode(reservationCode);

        Assertions.assertNotNull(reservation);
        Assertions.assertNotNull(reservation.getCode());
        // TODO: Add more assertions (over dates).

        // 3) Cancel reservation.
        reservationDto.setReservationCode(reservationCode);
        reservationService.cancelReservation(reservationDto);

        reservation = reservationService.getReservationByCode(reservationCode);
        Assertions.assertNotNull(reservation);
        Assertions.assertNotNull(reservation.getCode());
        // TODO: Add assertion for cancellation date.

        verify(calendarService, times(2)).updateCalendar(any());

    }

}
