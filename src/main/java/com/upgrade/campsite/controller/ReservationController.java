package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    @Autowired
    private ReservationService campsiteService;

    @PostMapping
    public Reservation bookReservation(@RequestBody ReservationDto reservationDto) throws ApiErrorException {
        return campsiteService.createOrModifyReservationAndNotifyUser(reservationDto);
    }

    @GetMapping
    public Reservation getReservationDetailsByCode(@RequestParam String code) throws ApiErrorException {
        return campsiteService.getReservationByCode(code);
    }

    @DeleteMapping
    public void cancelReservation(@RequestBody ReservationDto reservationDto) throws ApiErrorException {
        campsiteService.cancelReservation(reservationDto);
    }

}
