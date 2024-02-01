package com.d101.frientree.serviceImpl.calendar;

import com.d101.frientree.dto.calendar.request.CalendarMonthlyFruitsRequest;
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

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final UserRepository userRepository;
    private final UserFruitRepository userFruitRepository;
    @Override
    public ResponseEntity<?> monthlyFruits(CalendarMonthlyFruitsRequest request) {
        //월간 이미지 조회

        //사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findById(Long.valueOf(authentication.getName()));
        if(user.isEmpty()){throw new UserNotFoundException("User Not Found");}

        //시작 date 와 종료 date 기준으로 유저 열매 조회하기



        //DTO 담기

        //Response 형식으로 반환


        return null;
    }
}
