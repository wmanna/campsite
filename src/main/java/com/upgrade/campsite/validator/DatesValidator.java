package com.upgrade.campsite.validator;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.upgrade.campsite.exception.Constant.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.isNull;

@Component
public class DatesValidator {

    @Value( "${reservation.calendar.max}")
    private Integer maxDays;

    @Value( "${reservation.calendar.min}")
    private Integer minDays;

    @Value( "${reservation.duration.max}")
    private Integer maxDurationInDays;

    @Autowired
    private ReservationRepository reservationRepository;

    /* Validates consistency between the arrival and departure dates.
     * It also validates business rules (min and max) and overlapping. */
    public void validateDates(ReservationDto reservationDto) throws ApiErrorException {

        if (isNull(reservationDto.getArrivalDate()) || isNull(reservationDto.getDepartureDate())) {
            throw new ApiErrorException(REQUIRED_RESERVATION_DATES_ERROR_CODE, REQUIRED_RESERVATION_DATES_ERROR_MSG);
        }

        LocalDate today = LocalDate.now();

        if (reservationDto.getArrivalDate().isBefore(today) || DAYS.between(reservationDto.getArrivalDate(), reservationDto.getDepartureDate()) < 1) {
            throw new ApiErrorException(INVALID_RESERVATION_DATES_ERROR_CODE, INVALID_RESERVATION_DATES_ERROR_MSG);
        }

        if (DAYS.between(reservationDto.getArrivalDate(), reservationDto.getDepartureDate()) > maxDurationInDays) {
            throw new ApiErrorException(RESERVATION_MAX_DURATION_ERROR_CODE, RESERVATION_MAX_DURATION_ERROR_MSG);
        }

        if (DAYS.between(today, reservationDto.getArrivalDate()) < minDays || DAYS.between(today, reservationDto.getArrivalDate()) > maxDays) {
            throw new ApiErrorException(ARRIVAL_DATE_NOT_AVAILABLE_ERROR_CODE, ARRIVAL_DATE_NOT_AVAILABLE_ERROR_MSG);
        }

        List<Reservation> reservationList = reservationRepository
                .findByArrivalDateGreaterThanEqualAndArrivalDateLessThanEqualAndCancellationDateIsNullOrDepartureDateGreaterThanEqualAndDepartureDateLessThanEqualAndCancellationDateIsNull(
                        reservationDto.getArrivalDate().atStartOfDay(),
                        reservationDto.getArrivalDate().atStartOfDay(),
                        reservationDto.getDepartureDate().atStartOfDay(),
                        reservationDto.getDepartureDate().atStartOfDay()
                );

        if (!reservationList.isEmpty()) {
            throw new ApiErrorException(NOT_AVAILABLE_DAYS_ERROR_CODE, NOT_AVAILABLE_DAYS_ERROR_MSG);
        }
    }
}
