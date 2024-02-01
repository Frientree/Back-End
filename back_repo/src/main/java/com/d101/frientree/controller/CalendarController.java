package com.d101.frientree.controller;

import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping("/monthly-fruits")
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarDateRequest request) throws ParseException {
        return calendarService.monthlyFruits(request);
    }

    @PostMapping("/weekly-fruits")
    public ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(CalendarDateRequest request) throws ParseException{
        return calendarService.weeklyFruits(request);
    }
}
