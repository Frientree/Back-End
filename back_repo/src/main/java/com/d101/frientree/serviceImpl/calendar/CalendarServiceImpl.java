package com.d101.frientree.serviceImpl.calendar;

import com.d101.frientree.dto.calendar.dto.CalendarMonthlyFruitsDTO;
import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
import com.d101.frientree.dto.calendar.response.CalendarMonthlyFruitsResponse;
import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.user.UserNotFoundException;
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
    public ResponseEntity<CalendarMonthlyFruitsResponse> monthlyFruits(CalendarMonthlyFruitsRequest request) throws ParseException {
        //월간 이미지 조회

        //request startDate, endDate --> Date 타입으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(request.getStartDate());
        Date endDate = dateFormat.parse(request.getEndDate());

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date 와 종료 date 기준으로 유저 열매 조회하기
        List<UserFruit> userFruits = userFruitRepository.findAllByUser_UserIdAndUserFruitCreateDateBetween(
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
}
