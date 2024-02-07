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
import com.d101.frientree.entity.mongo.leaf.Leaf;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.leaf.CategoryNotFoundException;
import com.d101.frientree.exception.leaf.LeafNotFoundException;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.*;
import com.d101.frientree.repository.mongo.MongoLeafRepository;
import com.d101.frientree.service.LeafService;
import com.d101.frientree.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final MongoLeafRepository mongoLeafRepository;

    @Override
    public ResponseEntity<LeafConfirmationResponse> confirm(int leafCategoryValue) {
        User currentUser = getUser();
        Long userId = currentUser.getUserId();

        // 1. leaf_send와 leaf_receive 테이블에서 현재 로그인한 사용자가 보낸 및 받은 leaf_num 가져오기
        List<Long> sentAndReceivedLeafNums = new ArrayList<>();
        sentAndReceivedLeafNums.addAll(leafSendRepository.findSentLeafNumsByUserId(userId));
        sentAndReceivedLeafNums.addAll(leafReceiveRepository.findReceivedLeafNumsByUserId(userId));

        // 2. leaf_detail 테이블에서 leaf_category에 해당하는 이파리 중에서
        //    로그인한 사용자가 보낸 및 받은 leaf를 제외한 이파리들 가져오기
        LeafCategory selectedCategory = LeafCategory.findByValue(leafCategoryValue+ 1 );

        Optional<LeafDetail> leaves;

        if (sentAndReceivedLeafNums.isEmpty()) {
            // 선택한 카테고리에 해당하는 모든 LeafDetail 가져오기
            leaves = leafRepository.findByLeafCategoryOrderByLeafViewAsc(selectedCategory).stream().findFirst();
        } else {
            // 선택한 카테고리에 속하면서 sentAndReceivedLeafNums에 포함되지 않은 LeafDetail 가져오기
            leaves = leafRepository.findAllByLeafCategoryAndLeafNumNotInOrderByLeafViewAsc(
                            selectedCategory, sentAndReceivedLeafNums)
                    .stream()
                    .findFirst();
        }

        Optional<LeafDetail> selectedorderbyLeaf = leaves;

        // Optional을 사용하니까 isPresent 가 사용이 가능해서 코드 수정했습니다.
        if (selectedorderbyLeaf.isPresent()) {
            // 선택된 leaf의 leaf_view 값을 1 증가
            LeafDetail selectedLeaf = selectedorderbyLeaf.get();

            selectedLeaf.setLeafView(selectedLeaf.getLeafView() + 1);

            // leaf 업데이트
            leafRepository.save(selectedLeaf);

            // leaf_receive 테이블에 이파리 추가

            LeafReceive leafReceive = LeafReceive.createLeafReceive(selectedLeaf, currentUser);
            leafReceiveRepository.save(leafReceive);

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
            String description = messageResponse.getData();

            // LeafConfirmationResponse 객체 생성
            LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                    "Success",
                    LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(description)  // 또는 description를 이용하여 DTO를 생성
            );
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    @Transactional
    public ResponseEntity<LeafGenerationResponse> generate(LeafGenerationRequest leafGenerationRequest, String createDate) {
        //createDate String --> LocalDate 변경
        LocalDate date = LocalDate.parse(createDate);

        // user 정보를 받아올 때, 유효성 검사까지 함께하는 메서드를 가지고 와서 사용했습니다.
        User currentUser = getUser();

        LeafDetail newLeaf = LeafDetail.createLeafDetail(leafGenerationRequest);


        newLeaf.setLeafCreateDate(date);

        // LeafDetail 저장
        leafRepository.save(newLeaf);

        LeafSend leafSend = LeafSend.createLeafSend(newLeaf, currentUser);

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
                .orElseThrow(() -> new LeafNotFoundException("Leaf not found."));

        currentLeaf.setLeafComplain(currentLeaf.getLeafComplain() + 1);

        // 누적된 complain(신고)수가 5를 충족하면 이파리를 삭제
        if (currentLeaf.getLeafComplain() >= 5) {
            // leaf_send 에 저장된 이파리 삭제
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
        User currentUser = getUser();
            Long userId = currentUser.getUserId();

            // 1. leaf_send 테이블에서 user_id를 기준으로 leaf_num을 가져오기
            List<Long> leafNumList = leafSendRepository.findLeafNumsByUser(userId);

            // 보낸 이파리가 없어서 leafNumList가 비어있을 경우 예외처리
            if (leafNumList.isEmpty()) {
                throw new LeafNotFoundException("보낸 이파리를 찾을 수 없습니다.");
            }

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
                    totalLeafView);

            // response 반환
            return ResponseEntity.ok(response);

    }

    @Transactional
    @Override
    public void moveAndDeleteOldLeaves() {
        //이파리 전체 조회해서 7일이 지난 지난 이파리 들고오기
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(1);
        List<LeafDetail> oldLeafs = leafDetailRepository.findAllByLeafCreateDateBefore(sevenDaysAgo);

        //MongoDB에 저장하기
        oldLeafs.forEach(leafDetail -> {
            Leaf mongoLeaf = new Leaf(leafDetail);
            mongoLeafRepository.save(mongoLeaf);
        });

        //유저 이파리 연관관계 제거하기
        oldLeafs.forEach(leafDetail -> {
            //LeafReceive 제거
            leafReceiveRepository.deleteByLeafDetail(leafDetail);
            //LeafSend 제거
            leafSendRepository.deleteByLeafDetail(leafDetail);
        });

        //기간 지난 이파리 삭제하기
        leafRepository.deleteAll(oldLeafs);
    }


    private User getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User currentUser = userRepository.findById(Long.valueOf(userId)
                )
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (currentUser.getUserDisabled()) {
            throw new UserNotFoundException("user disabled");
        }

        return currentUser;
    }
}