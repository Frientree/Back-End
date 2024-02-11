package com.d101.frientree.serviceImpl.calendar;

import com.d101.frientree.dto.calendar.dto.*;
import com.d101.frientree.dto.calendar.request.CalendarDateRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarTodayFeelStatisticsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyFruitsResponse;
import com.d101.frientree.dto.calendar.response.CalendarWeeklyJuiceResponse;
import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.juice.UserJuice;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.exception.userfruit.UserFruitNotFoundException;
import com.d101.frientree.exception.userjuice.UserJuiceNotFoundException;
import com.d101.frientree.repository.fruit.UserFruitRepository;
import com.d101.frientree.repository.juice.UserJuiceRepository;
import com.d101.frientree.repository.user.UserRepository;
import com.d101.frientree.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final UserRepository userRepository;
    private final UserFruitRepository userFruitRepository;
    private final UserJuiceRepository userJuiceRepository;

    @Override
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarDateRequest request) {
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date 와 종료 date 기준으로 유저 열매 조회하기
        List<UserFruit> userFruits = userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                user.get().getUserId(), startDate, endDate);

        List<CalendarMonthlyFruitsDTO> calendarMonthlyFruitsDTOList = new ArrayList<>();

        LocalDate currentDate = startDate;

        while(!currentDate.isAfter(endDate)){
            String formattedDate = currentDate.toString();

            UserFruit matchingUserFruit = null;
            // 해당 날짜에 맞는 데이터 찾기
            for(UserFruit userFruit : userFruits){
                String findFruitDate = userFruit.getUserFruitCreateDate().toString();
                if(findFruitDate.equals(formattedDate)){
                    matchingUserFruit = userFruit;
                    break;
                }
            }
            //DTO 담기 (day = "2024-01-01", imageUrl = userFruits.CalendarImageUrl)
            if(matchingUserFruit != null){

                calendarMonthlyFruitsDTOList.add(
                        CalendarMonthlyFruitsDTO.createCalendarMonthlyFruitsDTO(
                                formattedDate,
                                matchingUserFruit
                        ));
            }else{ //DTO 담기 (day = "2024-01-01", imageUrl = "")
                calendarMonthlyFruitsDTOList.add(
                        CalendarMonthlyFruitsDTO.createCalendarMonthlyFruitsDTO(
                                formattedDate
                        ));
            }
            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }
        //Response 형식으로 저장
        CalendarMonthlyFruitsResponse response = CalendarMonthlyFruitsResponse
                .createCalendarMonthlyFruitsResponse(
                "Success", calendarMonthlyFruitsDTOList);

        //Response Entity 반환
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalendarTodayFeelStatisticsResponse> todayFeelStatistics(String todayDate) {
        //request todayDate --> LocalDate 타입으로 변환
        LocalDate today = LocalDate.parse(todayDate);

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
                        today.toString(),
                        userFruit.get().getFruitDetail().getFruitFeel(),
                        userFruitStatistics
                )
        );

        //Response Entity 반환
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalendarWeeklyJuiceResponse> weeklyJuice(CalendarDateRequest request) {
        //주간 주스 조회
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //endDate 기준으로 유저가 주스 생성한 것 가져오기
        Optional<UserJuice> userJuice = userJuiceRepository.findByUser_UserIdAndUserJuiceCreateDate(
                user.get().getUserId(), endDate);
        if(userJuice.isEmpty()){throw new UserJuiceNotFoundException("User Juice Not Found");}

        //juiceData DTO 담기
        CalendarWeeklyJuiceDetailDTO calendarWeeklyJuiceDetailDTO =
                CalendarWeeklyJuiceDetailDTO.createCalendarWeeklyJuiceDetailDTO(
                userJuice.get().getJuiceDetail(),
                userJuice.get().getUserJuiceMessage()
        );

        //start, end 날짜 기준으로 유저가 생성한 열매 정보들 가져오기
        List<UserFruit> userFruitList =
                userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                user.get().getUserId(), startDate, endDate
        );

        List<CalendarWeeklyJuiceFruitsGraphDTO> calendarWeeklyJuiceFruitsGraphDTOList = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)){
            String formattedDate = currentDate.toString();

            UserFruit matchingUserFruit = null;

            // 해당 날짜에 맞는 데이터 찾기
            for(UserFruit userFruit : userFruitList){
                String findFruitDate = userFruit.getUserFruitCreateDate().toString();
                if(findFruitDate.equals(formattedDate)){
                    matchingUserFruit = userFruit;
                    break;
                }
            }

            //DTO 담기 (day = "2024-01-01", imageUrl = "aws url", Score = 열매 점수)
            if(matchingUserFruit != null){
                calendarWeeklyJuiceFruitsGraphDTOList.add(
                        CalendarWeeklyJuiceFruitsGraphDTO.createCalendarWeeklyJuiceFruitGraphDTO(
                                formattedDate,
                                matchingUserFruit
                        )
                );
            }else{ //DTO 담기 (day = "2024-01-01", imageUrl = "", Score = 11L)
                calendarWeeklyJuiceFruitsGraphDTOList.add(
                        CalendarWeeklyJuiceFruitsGraphDTO.createCalendarWeeklyJuiceFruitGraphDTO(
                                formattedDate
                        )
                );
            }
            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }

        //DTO 2개 저장하는 DTO에 담기
        CalendarWeeklyJuiceDTO calendarWeeklyJuiceDTO =
                CalendarWeeklyJuiceDTO.createCalendarWeeklyJuiceDTO(
                calendarWeeklyJuiceDetailDTO,
                calendarWeeklyJuiceFruitsGraphDTOList
        );

        //Response 형식 저장
        CalendarWeeklyJuiceResponse response =
                CalendarWeeklyJuiceResponse.createCalendarWeeklyJuiceResponse(
                        "Success",
                        calendarWeeklyJuiceDTO
                );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalendarWeeklyFruitsResponse> weeklyFruits(CalendarDateRequest request) {
        //주간 열매 리스트 조회
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date, 종료 date 기준 유저 열매 조회
        List<UserFruit> userFruits = userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                user.get().getUserId(), startDate, endDate);

        List<CalendarWeeklyFruitsDTO> calendarWeeklyFruitsDTOList = new ArrayList<>();

        //DTO 담기
        for(UserFruit userFruit : userFruits){
            calendarWeeklyFruitsDTOList.add(
                    CalendarWeeklyFruitsDTO.createCalendarWeeklyFruitsDTO(
                            userFruit.getUserFruitCreateDate().toString(),
                            userFruit
                    )
            );
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
