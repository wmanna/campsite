package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.Reservation;
import com.upgrade.campsite.service.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class CampsiteController {

    @Autowired
    private CampsiteService campsiteService;

    @GetMapping("/")
    public String main() {
        return "Campsite API";
    }

    @GetMapping("/campsite")
    public Set<String> searchAvailableDates() {
        return campsiteService.search();
    }

    @PostMapping("/campsite")
    public Reservation bookReservation(@RequestBody ReservationDto reservationDto) {
        return campsiteService.book(reservationDto);
    }

    @GetMapping("/campsite/details")
    public Optional<Reservation> getReservationDetails(@RequestParam String id) {
        return campsiteService.getReservationById("test");
    }
}
