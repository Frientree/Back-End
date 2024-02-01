package com.d101.frientree.service;


import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface CalendarService {
    ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarDateRequest request) throws ParseException;
    ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(CalendarDateRequest request) throws ParseException;
}
