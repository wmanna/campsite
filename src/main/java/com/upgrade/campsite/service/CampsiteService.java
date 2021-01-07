package com.upgrade.campsite.service;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CampsiteService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceLockService resourceLockService;

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation book(ReservationDto reservationDto) {

        ResourceLock lock = resourceLockService.acquireLock();

        Reservation reservation = reservationRepository.findByCode(reservationDto.getReservationCode());

        if (Objects.isNull(reservation)) {
            reservation = new Reservation();
        } else {
            resourceLockService.release(lock);
            throw new RuntimeException("Reservation code already used or invalid.");
        }

        reservation.setReservationDate(LocalDateTime.now());
        reservation.setCode(reservationDto.getReservationCode());
        reservation = reservationRepository.save(reservation);

        resourceLockService.release(lock);

        return reservation;

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
