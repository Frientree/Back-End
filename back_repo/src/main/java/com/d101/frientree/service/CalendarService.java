package com.d101.frientree.service;


import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface CalendarService {
    ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarMonthlyFruitsRequest request) throws ParseException;
}
