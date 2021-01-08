package com.upgrade.campsite.service;

import static java.util.Objects.isNull;
import static java.time.temporal.ChronoUnit.DAYS;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CampsiteReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceLockService resourceLockService;

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation getNewReservation(ReservationDto reservationDto) throws ApiErrorException {

        // TODO: Use aspects to acquire/release lock.
        ResourceLock lock = resourceLockService.acquireLock();

        try {

            Reservation reservation = reservationRepository.findByCode(reservationDto.getReservationCode());

            if (Objects.isNull(reservation)) {
                reservation = new Reservation();
            }

            validateDates(reservationDto);
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setArrivalDate(reservationDto.getArrivalDate().atStartOfDay());
            reservation.setDepartureDate(reservationDto.getDepartureDate().atStartOfDay());
            reservation.setCode(getNewReservationCode(reservationDto));
            reservation = reservationRepository.save(reservation);

            return reservation;

        } finally {
            resourceLockService.release(lock);
        }

    }

    private String getNewReservationCode(ReservationDto reservationDto) {
        return "R" + new Random().nextInt(1000) + "-" + reservationDto.hashCode();
    }

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

        if (DAYS.between(today, reservationDto.getArrivalDate()) > 30) {
            throw new ApiErrorException(4005, "The arrival date is not available yet.");
        }

        if (DAYS.between(today, reservationDto.getArrivalDate()) < 1) {
            throw new ApiErrorException(4006, "Arrival date requires 1 day at least for check-in.");
        }

        // TODO: Validate dates overlapping.

    }

    public Set<String> search() {

        Set<String> availableDates = new TreeSet<>();

        // Mocks
        availableDates.add("2020-01-01");
        availableDates.add("2020-01-05");
        availableDates.add("2020-01-02");

        return availableDates;
    }
}
