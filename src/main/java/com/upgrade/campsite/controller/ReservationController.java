package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.service.CampsiteReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ReservationController {

    @Autowired
    private CampsiteReservationService campsiteService;

    @PostMapping("/campsite")
    public Reservation bookReservation(@RequestBody ReservationDto reservationDto) throws ApiErrorException {
        return campsiteService.getNewReservation(reservationDto);
    }

    @GetMapping("/campsite/details")
    public Optional<Reservation> getReservationDetails(@RequestParam String id) {
        return campsiteService.getReservationById(id);
    }
}
