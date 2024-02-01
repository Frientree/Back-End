package com.d101.frientree.serviceImpl;

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
import com.d101.frientree.repository.*;
import com.d101.frientree.service.JuiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuiceServiceImpl implements JuiceService {

    private final UserRepository userRepository;
    private final JuiceDetailRepository juiceDetailRepository;
    private final UserJuiceRepository userJuiceRepository;
    private final UserFruitRepository userFruitRepository;
    private final MessageRepository messageRepository;

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

    @Override
    public ResponseEntity<JuiceGenerationResponse> generate(JuiceGenerationRequest juiceGenerationRequest) throws ParseException {

        User currentUser = getUser();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(juiceGenerationRequest.getStartDate());
        Date endDate = dateFormat.parse(juiceGenerationRequest.getEndDate());

        // 만약 startDate가 일요일이 아니거나 endDate가 토요일이 아닐경우에 커스텀 예외처리
        if (!isSunday(startDate) || !isSaturday(endDate)) {
            throw new InvalidDateException("Invalid date range");
        }

        // 만약 startDate와 endDate가 일주일 이상 차이나면 커스텀 예외 처리
        if (endDate.getTime() - startDate.getTime() > 7 * 24 * 60 * 60 * 1000) {
            throw new InvalidDateException("Date range should be within 7 days");
        }

        // 해당 기간에 유저가 보유한 모든 과일을 가져온다.
        List<UserFruit> userFruits = userFruitRepository.findAllByUserAndUserFruitCreateDateBetween(currentUser, startDate, endDate);

        if (userFruits.size() < 4) {
            throw new JuiceGenerationException("Not enough fruits to generate juice");
        }

        // 유저가 주스를 만들기 위해 가져온 과일들이 들어간 dto
        List<JuiceFruitsGraphDataDTO> juiceFruitsGraphDataDTO = JuiceFruitsGraphDataDTO.createJuiceFruitsGraphDataDTO(userFruits);

        // 랜덤 주스를 생성 (아직 주스 디자인이 다 안나와서 그냥 랜덤주스 넣었습니다.)
        List<JuiceDetail> allJuices = juiceDetailRepository.findAll();
        // 랜덤한 주스 하나 선택
        JuiceDetail randomJuice = getRandomElement(allJuices);

        // Message data 중 랜덤으로 하나 가져와야 됨.
        List<Message> allMessages = messageRepository.findAll();
        // 랜덤한 메시지 하나 선택
        Message randomMessage = getRandomElement(allMessages);

        JuiceDataDTO juiceDataDTO = JuiceDataDTO.createJuiceDataDTO(randomJuice, randomMessage);

        JuiceGenerationResponse response = JuiceGenerationResponse.createJuiceGenerationResponse(
                "success",
                juiceDataDTO,
                juiceFruitsGraphDataDTO
        );

        UserJuice userJuice = UserJuice.builder()
                .user(currentUser)
                .juiceDetail(randomJuice)
                .userJuiceCreateDate(endDate)
                .build();

        userJuiceRepository.save(userJuice);

        return ResponseEntity.status(HttpStatus.OK).body(response);
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

    private Boolean isSunday(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY;
    }

    private Boolean isSaturday(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY;
    }
}
