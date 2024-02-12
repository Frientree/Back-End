package com.d101.frientree.serviceImpl.leaf;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.dto.leaf.response.LeafComplaintResponse;
import com.d101.frientree.dto.leaf.response.LeafConfirmationResponse;
import com.d101.frientree.dto.leaf.response.LeafGenerationResponse;
import com.d101.frientree.dto.leaf.response.LeafViewResponse;
import com.d101.frientree.dto.leaf.response.dto.LeafConfirmationResponseDTO;
import com.d101.frientree.dto.message.response.MessageResponse;
import com.d101.frientree.entity.leaf.LeafCategory;
import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.leaf.LeafReceive;
import com.d101.frientree.entity.leaf.LeafSend;
import com.d101.frientree.entity.mongo.leaf.Leaf;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.leaf.LeafNotFoundException;
import com.d101.frientree.exception.leaf.LeafCreationFailedException;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.leaf.LeafDetailRepository;
import com.d101.frientree.repository.leaf.LeafReceiveRepository;
import com.d101.frientree.repository.leaf.LeafSendRepository;
import com.d101.frientree.repository.mongo.MongoLeafRepository;
import com.d101.frientree.repository.user.UserRepository;
import com.d101.frientree.service.leaf.LeafService;
import com.d101.frientree.service.message.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LeafServiceImpl implements LeafService {
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
        String selectedCategory = LeafCategory.findLeafCategory(leafCategoryValue);

        Optional<LeafDetail> leaves;

        if (sentAndReceivedLeafNums.isEmpty()) {
            // 선택된 카테고리에 해당하는 LeafDetail 중에서 조회수(LeafView)가 가장 낮은 항목 한 개만 가져오기
            leaves = leafDetailRepository.findTopByLeafCategoryOrderByLeafViewAsc(selectedCategory);
        } else {
            // 선택한 카테고리에 속하면서 sentAndReceivedLeafNums에 포함되지 않은 LeafDetail 가져오기
            leaves = leafDetailRepository.findTopByLeafCategoryAndLeafNumNotInOrderByLeafViewAsc(
                            selectedCategory, sentAndReceivedLeafNums);
        }
        if (leaves.isPresent()) {
            // 선택된 leaf의 leaf_view 값을 1 증가
            LeafDetail selectedLeaf = leaves.get();

            selectedLeaf.setLeafView(selectedLeaf.getLeafView() + 1);

            // leaf 업데이트
            leafDetailRepository.save(selectedLeaf);

            // leaf_receive 테이블에 이파리 추가
            LeafReceive leafReceive = LeafReceive.createLeafReceive(selectedLeaf, currentUser);
            leafReceiveRepository.save(leafReceive);

            LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                    "Success",
                    LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(selectedLeaf)
            );
            return ResponseEntity.ok(response);
        }else{ // 더 이상 받을 이파리가 없을 때
            ResponseEntity<MessageResponse> messageResponseEntity = messageService.confirm();
            if (messageResponseEntity.getStatusCode() == HttpStatus.OK) {
                String description = messageResponseEntity.getBody().getData();

                // LeafConfirmationResponse 객체 생성
                LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                        "Success",
                        LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(description)
                );
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    @Transactional
    public ResponseEntity<LeafGenerationResponse> generate(LeafGenerationRequest leafGenerationRequest) {
        User currentUser = getUser();

        // 유저의 leaf_status가 0보다 큰 경우에만 이파리 생성 가능
        if(currentUser.getUserLeafStatus() > 0){

            LeafDetail newLeaf = LeafDetail.createLeafDetail(leafGenerationRequest);
            newLeaf.setLeafCreateDate(LocalDate.now());

            // LeafDetail 저장
            leafDetailRepository.save(newLeaf);
            LeafSend leafSend = LeafSend.createLeafSend(newLeaf, currentUser);

            // LeafSend 테이블에 추가
            leafSendRepository.save(leafSend);

            // 이파리 생성할 때 user의 leaf_status를 false로 변경
            currentUser.setUserLeafStatus(currentUser.getUserLeafStatus()-1);
            userRepository.save(currentUser);

            // LeafGenerationResponse 생성
            LeafGenerationResponse response = LeafGenerationResponse.createLeafGenerationResponse(
                    "Success",
                    currentUser.getUserLeafStatus()
            );

            // LeafGenerationResponse 반환
            return ResponseEntity.ok(response);
        }
        else {
            throw new LeafCreationFailedException("Leaf creation failed");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<LeafComplaintResponse> complain(Long leafId) {
        LeafDetail currentLeaf = leafDetailRepository.findById(leafId)
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
            leafDetailRepository.delete(currentLeaf);
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
            long totalLeafView = leafDetailRepository.getTotalLeafViewByUserId(userId);


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
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
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
        leafDetailRepository.deleteAll(oldLeafs);
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