package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.AvailableCalendar;
import com.upgrade.campsite.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CalendarController {

    @Autowired
    private CalendarService reservationCalendarService;

    @GetMapping("/campsite")
    public AvailableCalendar searchAvailableDates(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reservationCalendarService.search(startDate, endDate);
    }

}
