package com.d101.frientree.serviceImpl.calendar;

import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
import com.d101.frientree.service.CalendarService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CalendarServiceImpl implements CalendarService {
    @Override
    public ResponseEntity<?> monthlyFruits(CalendarMonthlyFruitsRequest request) {



        return null;
    }
}
