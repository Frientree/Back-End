package com.d101.frientree.controller;

import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyJuiceResponse;
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
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(@RequestBody CalendarDateRequest request) throws ParseException {
        return calendarService.monthlyFruits(request);
    }

    @PostMapping("/weekly-fruits")
    public ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(@RequestBody CalendarDateRequest request) throws ParseException{
        return calendarService.weeklyFruits(request);
    }

    @GetMapping("/today-feel-statistics")
    public ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(
            @RequestParam("todayDate") String todayDate) throws ParseException {
        return calendarService.todayFeelStatistics(todayDate);
    }

    @PostMapping("/weekly-juice")
    public ResponseEntity<CalendarWeeklyJuiceResponse> weeklyJuice(CalendarDateRequest request) throws ParseException{
        return calendarService.weeklyJuice(request);
    }
}
