package com.upgrade.campsite.unit;

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

import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ReservationServiceTests {

    @Mock
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

    private Reservation reservationMock;

    private ReservationDto reservationDto;

    private final String RESERVATION_CODE = "R123-123456";

    @BeforeEach
    public void init() {

        openMocks(this);

        ReflectionTestUtils.setField(datesValidator, "minDays", 1);
        ReflectionTestUtils.setField(datesValidator, "maxDays", 30);

        // Reservation Entity
        reservationMock = new Reservation();
        reservationMock.setCode(RESERVATION_CODE);

        // Reservation DTO
        reservationDto = new ReservationDto();
        reservationDto.setArrivalDate(LocalDate.now().plusDays(2));
        reservationDto.setDepartureDate(LocalDate.now().plusDays(4));
        reservationDto.setUserEmailAddress("test@email.com");
        reservationDto.setUserFullName("Test T. Test");

    }

    @Test
    void createReservationOkReturnsReservationCode() throws ApiErrorException {

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        Assertions.assertEquals(RESERVATION_CODE, reservationService.createOrModifyReservation(reservationDto).getCode());

        Assertions.assertNotNull(reservationMock.getCode());

        verify(reservationRepository, times(1)).save(any());
        verify(calendarService, times(1)).updateCalendar(any());

    }

    @Test
    void cancelReservation() throws ApiErrorException {

        when(reservationRepository.findByCode(any(String.class))).thenReturn(reservationMock);

        reservationDto.setReservationCode(RESERVATION_CODE);

        reservationService.cancelReservation(reservationDto);

        Assertions.assertNotNull(ReflectionTestUtils.getField(reservationMock, "cancellationDate"));
        Assertions.assertTrue((Boolean) ReflectionTestUtils.getField(reservationDto, "cancellation"));

        verify(reservationRepository, times(1)).save(any());
        verify(calendarService, times(1)).updateCalendar(any());

    }

}
