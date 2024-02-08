package com.d101.frientree.service;


import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyJuiceResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface CalendarService {
    ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarDateRequest request);
    ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(CalendarDateRequest request);
    ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(String todayDate);
    ResponseEntity<CalendarWeeklyJuiceResponse> weeklyJuice(CalendarDateRequest request);
}
