package com.upgrade.campsite.service;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CampsiteService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation book(ReservationDto reservationDto) {

        Reservation reservation = new Reservation();
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setCode("21R" + Math.random());
        return reservationRepository.save(reservation);

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
