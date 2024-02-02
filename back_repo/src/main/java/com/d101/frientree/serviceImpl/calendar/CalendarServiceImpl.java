package com.d101.frientree.serviceImpl.calendar;

import com.d101.frientree.dto.calendar.dto.CalendarMonthlyFruitsDTO;
import com.d101.frientree.dto.calendar.dto.CalendarTodayFeelStatisticsDTO;
import com.d101.frientree.dto.calendar.dto.CalendarWeeklyFruitsDTO;
import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyJuiceResponse;
import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.exception.userfruit.UserFruitNotFoundException;
import com.d101.frientree.repository.UserFruitRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final UserRepository userRepository;
    private final UserFruitRepository userFruitRepository;

    @Override
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarDateRequest request) throws ParseException {
        //request startDate, endDate --> Date 타입으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(request.getStartDate());
        Date endDate = dateFormat.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date 와 종료 date 기준으로 유저 열매 조회하기
        List<UserFruit> userFruits = userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                user.get().getUserId(), startDate, endDate);

        //day 값 setting (Calendar 객체로 날짜 더하는 기능 쓰기)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<CalendarMonthlyFruitsDTO> calendarMonthlyFruitsDTOList = new ArrayList<>();

        //DTO 담기 (day = "2024-01-01", imageUrl = userFruits.CalendarImageUrl)
        for(UserFruit userFruit : userFruits){
            Date day = calendar.getTime();
            calendarMonthlyFruitsDTOList.add(
                    CalendarMonthlyFruitsDTO.createCalendarMonthlyFruitsDTO(
                            dateFormat.format(day),
                            userFruit
                    ));
            calendar.add(Calendar.DATE, 1); //하루 더하기
        }
        //Response 형식으로 저장
        CalendarMonthlyFruitsResponse response = CalendarMonthlyFruitsResponse
                .createCalendarMonthlyFruitsResponse(
                "Success", calendarMonthlyFruitsDTOList);

        //Response Entity 반환
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(String todayDate) throws ParseException {
        //request todayDate --> Date 타입으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = dateFormat.parse(todayDate);

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //사용자가 금일 생성한 열매 감정 가져오기
        Optional<UserFruit> userFruit = userFruitRepository.findByUser_UserIdAndUserFruitCreateDate(Long.valueOf(authentication.getName()), today);
        if(userFruit.isEmpty()){throw new UserFruitNotFoundException("User Fruit Not Found");}

        //금일 생성한 열매 감정과 같은 사용자 수 가져오기
        Long userFruitStatistics = userFruitRepository
                .countByFruitNumAndDateExcludingUser(
                        userFruit.get().getFruitDetail().getFruitNum(),
                        today,
                        Long.valueOf(authentication.getName())
                );

        //DTO 담으면서 Response 형식으로 저장
        CalendarTodayFeelStatisticsResponse response = CalendarTodayFeelStatisticsResponse
                .createCalendarTodayFeelStatisticsResponse(
                "Success",
                CalendarTodayFeelStatisticsDTO.createCalendarTodayFeelStatisticsDTO(
                        dateFormat.format(today),
                        userFruit.get().getFruitDetail().getFruitFeel(),
                        userFruitStatistics
                )
        );

        //Response Entity 반환
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalendarWeeklyJuiceResponse> weeklyJuice(CalendarDateRequest request) throws ParseException {
        //주간 주스 조회

        //request startDate, endDate --> Date 타입으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(request.getStartDate());
        Date endDate = dateFormat.parse(request.getEndDate());





        return null;
    }

    @Override
    public ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(CalendarDateRequest request) throws ParseException {
        //주간 열매 리스트 조회

        //request startDate, endDate --> Date 타입으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(request.getStartDate());
        Date endDate = dateFormat.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date, 종료 date 기준 유저 열매 조회
        List<UserFruit> userFruits = userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                user.get().getUserId(), startDate, endDate);

        //day 값 setting (Calendar 객체로 날짜 더하는 기능 쓰기)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<CalendarWeeklyFruitsDTO> calendarWeeklyFruitsDTOList = new ArrayList<>();

        //DTO 담기
        for(UserFruit userFruit : userFruits){
            Date day =  calendar.getTime();

            calendarWeeklyFruitsDTOList.add(
                    CalendarWeeklyFruitsDTO.createCalendarWeeklyFruitsDTO(
                            dateFormat.format(day),
                            userFruit
                    )
            );
            calendar.add(Calendar.DATE, 1); //하루 더하기
        }

        //Response 형식 저장
        CalendarWeeklyFruitsResponse response = CalendarWeeklyFruitsResponse
                .createCalendarWeeklyFruitsResponse(
                        "Success",
                        calendarWeeklyFruitsDTOList);

        //Response 반환
        return ResponseEntity.ok(response);
    }
}
