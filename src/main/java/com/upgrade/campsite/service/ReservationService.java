package com.upgrade.campsite.service;

import com.upgrade.campsite.exception.Constant;
import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationRepository;
import com.upgrade.campsite.validator.DatesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CalendarService reservationCalendarService;

    @Autowired
    private DatesValidator datesValidator;

    @Autowired
    private ResourceLockService resourceLockService;

    public Reservation getReservationByCode(String code) throws ApiErrorException {

        if (Objects.isNull(code)) {
            throw new ApiErrorException(Constant.REQUIRED_RESERVATION_CODE, Constant.REQUIRED_RESERVATION_CODE_MSG);
        }

        Reservation reservation = reservationRepository.findByCode(code);
        if (Objects.isNull(reservation)) {
            throw new ApiErrorException(Constant.RESERVATION_NOT_FOUND_ERROR_CODE, Constant.RESERVATION_NOT_FOUND_ERROR_MSG);
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

            if (Objects.isNull(dto.getReservationCode())) {
                // Create new Reservation
                reservation = new Reservation();
                reservation.setCode(getNewReservationCode(dto));

            } else {
                // Modify Reservation
                reservation = getReservationByCode(dto.getReservationCode());
            }

            datesValidator.validateDates(dto);

            reservation.setUser(userService.getUser(dto));
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setArrivalDate(dto.getArrivalDate().atStartOfDay());
            reservation.setDepartureDate(dto.getDepartureDate().atStartOfDay());

            reservation = reservationRepository.save(reservation);

            // Update calendar -> Read model synchronization
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
     *  Example of reservation code: R123-1234568. */
    private String getNewReservationCode(ReservationDto reservationDto) {
        return "R" + new Random().nextInt(1000) + "-" + reservationDto.hashCode();
    }

}
