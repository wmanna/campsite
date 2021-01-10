package com.upgrade.campsite.service;

import static java.util.Objects.isNull;
import static java.time.temporal.ChronoUnit.DAYS;

import com.upgrade.campsite.Constant;
import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {

    @Value( "${reservation.calendar.max}")
    private Integer maxDays;

    @Value( "${reservation.calendar.min}")
    private Integer minDays;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CalendarService reservationCalendarService;

    @Autowired
    private ResourceLockService resourceLockService;

    public Reservation getReservationByCode(String code) throws ApiErrorException {

        if (Objects.isNull(code)) {
            throw new ApiErrorException(Constant.REQUIRED_RESERVATION_CODE, Constant.REQUIRED_RESERVATION_CODE_MSG);
        }

        Reservation reservation = reservationRepository.findByCode(code);
        if (Objects.isNull(reservation)) {
            throw new ApiErrorException(Constant.RESERVATION_NOT_FOUND_CODE, Constant.RESERVATION_NOT_FOUND_MSG);
        }

        return reservation;
    }

    /* Creates or modify a reservation and returns a Reservation entity that contains the code.
     * It uses an exclusive resource lock. */
    @Transactional(rollbackFor = Exception.class)
    public Reservation createOrModifyReservation(ReservationDto dto) throws ApiErrorException {

        // TODO: Use aspects to acquire/release lock.
        ResourceLock lock = resourceLockService.acquireLock();

        Reservation reservation;

        try {

            if (!Objects.isNull(dto.getReservationCode())) { // Modify
               reservation = getReservationByCode(dto.getReservationCode());

            } else {
                reservation = new Reservation(); // Create new
                reservation.setCode(getNewReservationCode(dto));
            }

            validateDates(dto);

            reservation.setUser(userService.getUser(dto));

            reservation.setReservationDate(LocalDateTime.now());
            reservation.setArrivalDate(dto.getArrivalDate().atStartOfDay());
            reservation.setDepartureDate(dto.getDepartureDate().atStartOfDay());

            reservation = reservationRepository.save(reservation);

            // Update calendar
            dto.setReservationCode(reservation.getCode());
            reservationCalendarService.updateCalendar(dto);

            return reservation;

        } finally {
            resourceLockService.release(lock);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(ReservationDto dto) throws ApiErrorException {

        Reservation reservation = this.getReservationByCode(dto.getReservationCode());
        reservation.setCancellationDate(LocalDateTime.now());
        reservationRepository.save(reservation);
        dto.setCancellation(true);
        reservationCalendarService.updateCalendar(dto);

    }

    /* Creates a random code for a new reservation.
     *  Example of generated code: R123-1234568. */
    private String getNewReservationCode(ReservationDto reservationDto) {
        return "R" + new Random().nextInt(1000) + "-" + reservationDto.hashCode();
    }

    /* Validates consistency between the arrival and departure dates.
     * It also validates business rules for them and overlapping with other reservations. */
    private void validateDates(ReservationDto reservationDto) throws ApiErrorException {

        if (isNull(reservationDto.getArrivalDate()) || isNull(reservationDto.getDepartureDate())) {
            throw new ApiErrorException(4003, "Arrival and departure dates can not be null or empty.");
        }

        LocalDate today = LocalDate.now();

        if (reservationDto.getArrivalDate().isBefore(today) || DAYS.between(reservationDto.getArrivalDate(), reservationDto.getDepartureDate()) < 1) {
            throw new ApiErrorException(4006, "Invalid reservation dates.");
        }

        if (DAYS.between(reservationDto.getArrivalDate(), reservationDto.getDepartureDate()) > 3) {
            throw new ApiErrorException(4004, "Max. 3 days exceeded.");
        }

        if (DAYS.between(today, reservationDto.getArrivalDate()) > maxDays) {
            throw new ApiErrorException(4005, "The arrival date is not available yet.");
        }

        if (DAYS.between(today, reservationDto.getArrivalDate()) < minDays) {
            throw new ApiErrorException(4006, "Arrival date requires" + minDays + " + day/s at least for check-in.");
        }

        if (!findReservationsBetweenDates(reservationDto.getArrivalDate(), reservationDto.getDepartureDate()).isEmpty()) {
            throw new ApiErrorException(4007, "One or more days are not available.");
        }

    }

    private List<Reservation> findReservationsBetweenDates(LocalDate beginDate, LocalDate endDate) {
        return reservationRepository.findByArrivalDateGreaterThanEqualAndArrivalDateLessThanEqualAndCancellationDateIsNullOrDepartureDateGreaterThanEqualAndDepartureDateLessThanEqualAndCancellationDateIsNull(beginDate.atStartOfDay(), beginDate.atStartOfDay(), endDate.atStartOfDay(), endDate.atStartOfDay());
    }

}
