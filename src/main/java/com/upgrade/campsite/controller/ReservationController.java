package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    @Autowired
    private ReservationService campsiteService;

    @PostMapping("/campsite")
    public Reservation bookReservation(@RequestBody ReservationDto reservationDto) throws ApiErrorException {
        return campsiteService.createOrModifyReservation(reservationDto);
    }

    @GetMapping("/campsite/details")
    public Reservation getReservationDetailsByCode(@RequestParam String code) {
        return campsiteService.getReservationByCode(code);
    }
}
