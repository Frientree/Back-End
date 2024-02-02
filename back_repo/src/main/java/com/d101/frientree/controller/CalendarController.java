package com.d101.frientree.controller;

import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyJuiceResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    @Operation(summary = "유저 열매 월간 이미지 조회", description = "startDate, endDate 기준으로 유저가 생성한 캘린더 열매 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = CalendarMonthlyFruitsResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"User Not Found\", code : 404)")
    })
    @PostMapping("/monthly-fruits")
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(@RequestBody CalendarDateRequest request) throws ParseException {
        return calendarService.monthlyFruits(request);
    }

    @Operation(summary = "유저 열매 주간 이미지 조회", description = "startDate, endDate 기준으로 유저가 생성한 캘린더 열매 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = CalendarWeeklyFruitsResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"User Not Found\", code : 404)")
    })
    @PostMapping("/weekly-fruits")
    public ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(@RequestBody CalendarDateRequest request) throws ParseException{
        return calendarService.weeklyFruits(request);
    }

    @Operation(summary = "금일 열매 통계", description = "금일 사용자가 생성한 열매와 오늘 다른 사용자가 생성한 열매가 몇 명이 같은지 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = CalendarTodayFeelStatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"User Fruit Not Found\", code : 404)")
    })
    @GetMapping("/today-feel-statistics")
    public ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(
            @RequestParam("todayDate") String todayDate) throws ParseException {
        return calendarService.todayFeelStatistics(todayDate);
    }

    @Operation(summary = "주간 주스 조회", description = ".")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = CalendarWeeklyJuiceResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"User Not Found\", code : 404)\n" +
                    "\n" +
                    "(message : \"User Juice Not Found\", code : 404)")
    })
    @PostMapping("/weekly-juice")
    public ResponseEntity<CalendarWeeklyJuiceResponse> weeklyJuice(@RequestBody CalendarDateRequest request) throws ParseException{
        return calendarService.weeklyJuice(request);
    }
}
