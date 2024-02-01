package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.dto.leaf.response.LeafComplaintResponse;
import com.d101.frientree.dto.leaf.response.LeafConfirmationResponse;
import com.d101.frientree.dto.leaf.response.LeafGenerationResponse;
import com.d101.frientree.dto.leaf.response.LeafViewResponse;
import com.d101.frientree.dto.leaf.response.dto.LeafConfirmationResponseDTO;
import com.d101.frientree.dto.message.response.MessageResponse;
import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.leaf.LeafReceive;
import com.d101.frientree.entity.leaf.LeafSend;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.leaf.LeafNotFoundException;
import com.d101.frientree.repository.*;
import com.d101.frientree.service.LeafService;
import com.d101.frientree.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LeafServiceImpl implements LeafService {

    private final LeafRepository leafRepository;
    private final LeafSendRepository leafSendRepository;
    private final LeafReceiveRepository leafReceiveRepository;
    private final UserRepository userRepository;
    private final LeafDetailRepository leafDetailRepository;
    private final MessageService messageService;

    @Override
    public ResponseEntity<LeafConfirmationResponse> confirm(String leafCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 로그인한 사용자의 userId를 받아오기
        Long userId = Long.parseLong(authentication.getName());

        // 1. leaf_send와 leaf_receive 테이블에서 현재 로그인한 사용자가 보낸 및 받은 leaf_num 가져오기
        List<Long> sentAndReceivedLeafNums = new ArrayList<>();
        sentAndReceivedLeafNums.addAll(leafSendRepository.findSentLeafNumsByUserId(userId));
        sentAndReceivedLeafNums.addAll(leafReceiveRepository.findReceivedLeafNumsByUserId(userId));

        // 2. leaf_detail 테이블에서 leaf_category에 해당하는 이파리 중에서
        //    로그인한 사용자가 보낸 및 받은 leaf를 제외한 이파리들 가져오기
        List<LeafDetail> leaves = leafRepository.findByLeafCategoryAndLeafNumNotIn(
                LeafCategory.valueOf(leafCategory.toUpperCase()), sentAndReceivedLeafNums);

        if (!leaves.isEmpty()) {
            // leaf_view 값을 낮은 순서로 정렬
            leaves.sort(Comparator.comparing(LeafDetail::getLeafView));

            // 정렬된 leaves 중에서 가장 낮은 leaf_view를 가진 leaf 선택
            LeafDetail selectedLeaf = leaves.get(0);

            // 선택된 leaf의 leaf_view 값을 1 증가시킴
            selectedLeaf.setLeafView(selectedLeaf.getLeafView() + 1);

            // leaf를 업데이트
            leafRepository.save(selectedLeaf);

            // 현재 로그인된 정보를 받아온 후 조회한 이파리를 leaf_receive테이블에 추가하기
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("해당하는 유저를 찾을 수 없습니다."));

            // LeafReceive 테이블에 중복 체크를 위한 existsByUserAndLeafDetail 메서드 사용
            boolean leafExists = leafReceiveRepository.existsByUserAndLeafDetail(user, selectedLeaf);

            if (!leafExists) {
                // 중복되지 않으면 LeafReceive 테이블에 추가
                LeafReceive leafReceive = LeafReceive.createLeafReceive(selectedLeaf, user);
                leafReceiveRepository.save(leafReceive);
            }

            // LeafConfirmationResponse 객체 생성
            LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                    "Success",
                    LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(selectedLeaf)
            );
            return ResponseEntity.ok(response);
        }

        // 더 이상 받을 이파리가 없을 때 MessageResponse의 description을 가져와서 LeafConfirmationResponse 형식으로 반환
        ResponseEntity<MessageResponse> messageResponseEntity = messageService.confirm();
        MessageResponse messageResponse = messageResponseEntity.getBody();

        if (messageResponse != null) {
            String description = messageResponse.getDescription();

            // LeafConfirmationResponse 객체 생성
            LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                    "Success",
                    LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(description)  // 또는 description를 이용하여 DTO를 생성
            );
            return ResponseEntity.ok(response);
        }

        // 예외 처리 또는 다른 로직을 추가하십시오.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    @Transactional
    public ResponseEntity<LeafGenerationResponse> generate(LeafGenerationRequest leafGenerationRequest) {

        // 혅재 접속한 정보를 contextholder 에 담아놓은 정보를 가지고 오는 것

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        LocalDateTime leafCreateDate = LocalDateTime.now();

        LeafDetail newLeaf = LeafDetail.createLeafDetail(leafGenerationRequest);
        newLeaf.setLeafCreateDate(Date.from(leafCreateDate.atZone(ZoneId.systemDefault()).toInstant()));

        // LeafDetail 저장
        leafRepository.save(newLeaf);

        // LeafSend 테이블에 추가할 정보 설정
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 유저를 찾을 수 없습니다. "));

        LeafSend leafSend = LeafSend.createLeafSend(newLeaf, user);

        // LeafSend 테이블에 추가
        leafSendRepository.save(leafSend);

        // LeafGenerationResponse 생성
        LeafGenerationResponse response = LeafGenerationResponse.createLeafGenerationResponse(
                "Success",
                true
        );

        // LeafGenerationResponse 반환
        return ResponseEntity.ok(response);

    }

    @Override
    @Transactional
    public ResponseEntity<LeafComplaintResponse> complain(Long leafId) {
        LeafDetail currentLeaf = leafRepository.findById(leafId)
                .orElseThrow(() -> new LeafNotFoundException("이파리는 존재하지 않습니다."));

        currentLeaf.setLeafComplain(currentLeaf.getLeafComplain() + 1);

        // 누적된 complain(신고)수가 5를 충족하면 이파리를 삭제합니다.

        if (currentLeaf.getLeafComplain() >= 5) {
            // 삭제할 LeafDetail의 leaf_send 에 저장된 이파리 삭제
            leafSendRepository.deleteByLeafDetail(currentLeaf);

            // leaf_receive 에 저장된 이파리 삭제
            List<LeafReceive> relatedReceives = leafReceiveRepository.findByLeafDetail(currentLeaf);
            leafReceiveRepository.deleteAll(relatedReceives);

            // LeafDetail에 저장된 이파리 삭제
            leafRepository.delete(currentLeaf);
        }

        LeafComplaintResponse response = LeafComplaintResponse.createLeafComplaintResponse(
                "Success",
                true
        );

        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public ResponseEntity<LeafViewResponse> view() {
        // security를 이용해 로그인 정보를 받아옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인이 되어있지 않으면 에러 발생 -> 로그인 요청
        if (!isUserLoggedIn(authentication)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LeafViewResponse.createLeafViewErrorResponse(
                            "User not Logged In"
                    ));
        }

        try {
            Long authenticatedUserId = Long.parseLong(authentication.getName());

            // 1. leaf_send 테이블에서 user_id를 기준으로 leaf_num을 가져오기
            List<Long> leafNumList = leafSendRepository.findLeafNumsByUser(authenticatedUserId);

            // 2. leaf_detail 테이블에서 leaf_num에 해당하는 leaf_view 값 모두 더하기
            long totalLeafView = leafNumList.stream()
                    .mapToLong(leafNum -> {
                        LeafDetail leafDetail = leafDetailRepository.findByLeafNum(leafNum);
                        return leafDetail != null ? leafDetail.getLeafView() : 0;
                    })
                    .sum();

            // LeafViewResponse를 생성하고 반환
            LeafViewResponse response = LeafViewResponse.createLeafViewResponse(
                    "Success",
                    Long.valueOf(totalLeafView));

            // response 반환
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 예외 발생 시 에러 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    LeafViewResponse.createLeafViewErrorResponse("Internal Server Error")
            );
        }
    }

    // 유저가 로그인이 되어있는지 확인
    private boolean isUserLoggedIn(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

}