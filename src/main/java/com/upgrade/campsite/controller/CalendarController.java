package com.upgrade.campsite.controller;

import com.upgrade.campsite.dto.AvailableCalendarDto;
import com.upgrade.campsite.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService reservationCalendarService;

    @GetMapping
    public AvailableCalendarDto searchAvailableDates(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reservationCalendarService.search(startDate, endDate);
    }

}
