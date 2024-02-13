package com.d101.frientree.serviceImpl.juice;

import com.d101.frientree.dto.juice.request.JuiceGenerationRequest;
import com.d101.frientree.dto.juice.response.JuiceConfirmationResponse;
import com.d101.frientree.dto.juice.response.JuiceGenerationResponse;
import com.d101.frientree.dto.juice.response.JuiceListConfirmationResponse;
import com.d101.frientree.dto.juice.response.dto.*;
import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.juice.JuiceDetail;
import com.d101.frientree.entity.juice.UserJuice;
import com.d101.frientree.entity.message.Message;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.juice.InvalidDateException;
import com.d101.frientree.exception.juice.JuiceGenerationException;
import com.d101.frientree.exception.juice.JuiceNotFoundException;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.fruit.UserFruitRepository;
import com.d101.frientree.repository.juice.JuiceDetailRepository;
import com.d101.frientree.repository.juice.JuiceMessageRepository;
import com.d101.frientree.repository.juice.UserJuiceRepository;
import com.d101.frientree.repository.message.MessageRepository;
import com.d101.frientree.repository.user.UserRepository;
import com.d101.frientree.service.juice.JuiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuiceServiceImpl implements JuiceService {

    private final UserRepository userRepository;
    private final JuiceDetailRepository juiceDetailRepository;
    private final UserJuiceRepository userJuiceRepository;
    private final UserFruitRepository userFruitRepository;
    private final JuiceMessageRepository juiceMessageRepository;

    @Override
    public ResponseEntity<JuiceListConfirmationResponse> listConfirm() {

        User currentUser = getUser();
        // 모든 주스
        List<JuiceDetail> allJuices = juiceDetailRepository.findAll();
        // 유저가 가진 모든 주스
        List<UserJuice> userJuices = userJuiceRepository.findAllByUser(currentUser);

        // 모든 주스를 순회하면서 각 주스에 대한 현재 유저 보유 여부를 판단한 뒤 해당 정보와 각 주스 정보를 dto에 같이 넣어서 보유여부가 포함된 도감리스트를 반환합니다.
        List<JuiceListConfirmationResponseDTO> juiceList = allJuices.stream().map(juiceDetail -> {
            // 해당 주스를 유저가 가지고 있는지 없는지
            boolean juiceOwn = userJuices.stream().anyMatch(userJuice -> userJuice.getJuiceDetail().equals(juiceDetail));
            return JuiceListConfirmationResponseDTO.createJuiceListConfirmationResponseDTO(juiceDetail, juiceOwn);
        }).collect(Collectors.toList());

        JuiceListConfirmationResponse response = JuiceListConfirmationResponse.createJuiceListConfirmationResponse(
                "Success",
                juiceList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<JuiceConfirmationResponse> confirm(Long juiceId) {
        JuiceDetail currentJuice = juiceDetailRepository.findById(juiceId)
                .orElseThrow(() -> new JuiceNotFoundException("juice not found"));

        JuiceConfirmationResponse response = JuiceConfirmationResponse.createJuiceConfirmationResponse(
                "Success",
                JuiceConfirmationResponseDTO.createJuiceConfirmationResponseDTO(currentJuice)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    @Override
    public ResponseEntity<JuiceGenerationResponse> generate(JuiceGenerationRequest juiceGenerationRequest){

        User currentUser = getUser();
        LocalDate startDate = LocalDate.parse(juiceGenerationRequest.getStartDate());
        LocalDate endDate = LocalDate.parse(juiceGenerationRequest.getEndDate());


        // 만약 해당 기간 사이에서 주스를 조회해서 이미 만든게 있으면 커스텀 예외처리
        Optional<UserJuice> existJuice = userJuiceRepository.findByUser_UserIdAndUserJuiceCreateDate(currentUser.getUserId(), endDate);

        if (existJuice.isPresent()) {
            throw new JuiceGenerationException("Juice already generated for the given date range");
        }

        // 만약 endDate가 미래면 커스텀 예외처리
        if (LocalDate.now().isBefore(endDate)) {
            throw new InvalidDateException("End date cannot be in the future");
        }

        // 만약 startDate가 일요일이 아니거나 endDate가 토요일이 아닐경우에 커스텀 예외처리
        if (!isSunday(startDate) || !isSaturday(endDate)) {
            throw new InvalidDateException("Invalid date range");
        }

        // startDate와 endDate 사이의 일수를 계산합니다.
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // 만약 일수 차이가 7일 이상이라면 커스텀 예외를 발생시킵니다.
        if (daysBetween > 7) {
            throw new InvalidDateException("Date range should be within 7 days");
        }

        // 해당 기간에 유저가 보유한 모든 과일을 가져온다.
        List<UserFruit> userFruits = userFruitRepository.
                findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(
                        currentUser.getUserId(), startDate, endDate);

        if (userFruits.size() < 4) {
            throw new JuiceGenerationException("Not enough fruits to generate juice");
        }

        // 만약 userFruits 중에 사과가 하나라도 있으면 행운의 사과주스 생성
        boolean haveApple = false;
        for (UserFruit userFruit: userFruits) {
            if (userFruit.getUserFruitScore() == 22) {
                haveApple = true;
                break;
            }
        }

        if (haveApple) {
            JuiceDetail createdJuice = juiceDetailRepository.findJuicesByScore(0L);
            List<JuiceFruitsGraphDataDTO> juiceFruitsGraphDataDTO =
                    JuiceFruitsGraphDataDTO.createJuiceFruitsGraphDataDTO(startDate, endDate, userFruits);
            String juiceMessage = juiceMessageRepository.findRandomMessage(createdJuice.getJuiceNum());
            UserJuice userJuice = UserJuice.builder()
                    .user(currentUser)
                    .juiceDetail(createdJuice)
                    .userJuiceCreateDate(endDate)
                    .userJuiceMessage(juiceMessage)
                    .build();

            JuiceDataDTO juiceDataDTO = JuiceDataDTO.createJuiceDataDTO(createdJuice, juiceMessage);

            JuiceGenerationResponse response = JuiceGenerationResponse.createJuiceGenerationResponse(
                    "success",
                    juiceDataDTO,
                    juiceFruitsGraphDataDTO
            );
            userJuiceRepository.save(userJuice);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        long scoreSum = userFruits.stream()
                .mapToLong(UserFruit::getUserFruitScore)
                .sum();

        // 최종 스코어
        JuiceDetail createdJuice = juiceDetailRepository.findJuicesByScore(scoreSum);

        // 유저가 주스를 만들기 위해 가져온 과일들이 들어간 dto
        List<JuiceFruitsGraphDataDTO> juiceFruitsGraphDataDTO =
                JuiceFruitsGraphDataDTO.createJuiceFruitsGraphDataDTO(startDate, endDate, userFruits);

        // 주스 위로 메세지에서 createJuice의 juiceNum에 접근 -> 해당 juiceNum인 message 중에서 랜덤으로 하나 가져오기
        String juiceMessage = juiceMessageRepository.findRandomMessage(createdJuice.getJuiceNum());


        UserJuice userJuice = UserJuice.builder()
                .user(currentUser)
                .juiceDetail(createdJuice)
                .userJuiceCreateDate(endDate)
                .userJuiceMessage(juiceMessage)
                .build();

        JuiceDataDTO juiceDataDTO = JuiceDataDTO.createJuiceDataDTO(createdJuice, juiceMessage);

        JuiceGenerationResponse response = JuiceGenerationResponse.createJuiceGenerationResponse(
                "success",
                juiceDataDTO,
                juiceFruitsGraphDataDTO
        );


        userJuiceRepository.save(userJuice);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User currentUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (currentUser.getUserDisabled()) {
            throw new UserNotFoundException("User disabled");
        }

        return currentUser;
    }

    private <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    private Boolean isSunday(LocalDate startDate) {
        return startDate.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private Boolean isSaturday(LocalDate endDate) {
        return endDate.getDayOfWeek() == DayOfWeek.SATURDAY;
    }
}
