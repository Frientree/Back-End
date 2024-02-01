package com.d101.frientree.controller;

import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping("/monthly-fruits")
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarMonthlyFruitsRequest request) throws ParseException {
        return calendarService.monthlyFruits(request);
    }

    @GetMapping("/today-feel-statistics")
    public ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(
            @RequestParam("todayDate") String todayDate) throws ParseException {
        return calendarService.todayFeelStatistics(todayDate);
    }
}
