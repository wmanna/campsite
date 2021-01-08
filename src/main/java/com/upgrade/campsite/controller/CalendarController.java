package com.upgrade.campsite.controller;

import com.upgrade.campsite.service.CampsiteReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class CalendarController {

    @Autowired
    private CampsiteReservationService campsiteService;

    @GetMapping("/campsite")
    public Set<String> searchAvailableDates() {
        return campsiteService.search();
    }

}
