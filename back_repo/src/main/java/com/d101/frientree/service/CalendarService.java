package com.d101.frientree.service;


import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
import org.springframework.http.ResponseEntity;

public interface CalendarService {
    ResponseEntity<?> monthlyFruits(CalendarMonthlyFruitsRequest request);
}
