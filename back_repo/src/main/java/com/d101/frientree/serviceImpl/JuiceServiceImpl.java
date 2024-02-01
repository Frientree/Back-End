package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.juice.request.JuiceGenerationRequest;
import com.d101.frientree.dto.juice.response.JuiceConfirmationResponse;
import com.d101.frientree.dto.juice.response.JuiceGenerationResponse;
import com.d101.frientree.dto.juice.response.JuiceListConfirmationResponse;
import com.d101.frientree.dto.juice.response.dto.JuiceConfirmationResponseDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceGenerationResponseDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceListConfirmationResponseDTO;
import com.d101.frientree.entity.juice.JuiceDetail;
import com.d101.frientree.entity.juice.UserJuice;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.juice.JuiceNotFoundException;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.JuiceDetailRepository;
import com.d101.frientree.repository.UserJuiceRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.service.JuiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuiceServiceImpl implements JuiceService {

    private final UserRepository userRepository;
    private final JuiceDetailRepository juiceDetailRepository;
    private final UserJuiceRepository userJuiceRepository;

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

    private User getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User currentUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("Fail"));

        if (currentUser.getUserDisabled()) {
            throw new UserNotFoundException("Fail");
        }

        return currentUser;
    }
}
