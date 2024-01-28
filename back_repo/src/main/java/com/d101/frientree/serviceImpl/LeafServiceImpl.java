package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.dto.leaf.response.LeafComplaintResponse;
import com.d101.frientree.dto.leaf.response.LeafConfirmationResponse;
import com.d101.frientree.dto.leaf.response.LeafGenerationResponse;
import com.d101.frientree.dto.leaf.response.LeafViewResponse;
import com.d101.frientree.dto.leaf.response.dto.LeafComplaintResponseDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafGenerationResponseDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafConfirmationResponseDTO;
import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.LeafDetail;
import com.d101.frientree.entity.LeafSend;
import com.d101.frientree.entity.User;
import com.d101.frientree.repository.LeafRepository;
import com.d101.frientree.repository.LeafSendRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.service.LeafService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafServiceImpl implements LeafService {

    private final LeafRepository leafRepository;
    private final LeafSendRepository leafSendRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<LeafConfirmationResponse> confirm(String leafCategory) {
        List<LeafDetail> leaves = leafRepository.findByLeafCategory(LeafCategory.valueOf(leafCategory.toUpperCase()));

        if (!leaves.isEmpty()) {
            // leaf_view 값을 낮은 순서로 정렬
            leaves.sort(Comparator.comparing(LeafDetail::getLeafView));

            // 정렬된 leaves 중에서 가장 낮은 leaf_view를 가진 leaf 선택
            LeafDetail selectedLeaf = leaves.get(0);

            // 선택된 leaf의 leaf_view 값을 1 증가시킴
            selectedLeaf.setLeafView(selectedLeaf.getLeafView() + 1);

            // leaf를 업데이트
            leafRepository.save(selectedLeaf);

            LeafConfirmationResponse response = LeafConfirmationResponse.createLeafConfirmationResponse(
                    "Success",
                    LeafConfirmationResponseDTO.createLeafConfirmationResponseDTO(selectedLeaf)
            );

            // 선택된 leaf를 LeafReadResponseDTO로 변환하여 반환
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // 찾는 leaf가 없는 경우 null 반환
        return null;
//   이파리가 없는 경우(서비스초기) default leaf -> 예비로 넣어놓기 처리할 것
//   default leaf 를 던져줄 수 있게 처리...
    }

    @Override
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
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<LeafComplaintResponse> complain(Long leafId) {
        // TODO: 에러 처리 추가해야함
        LeafDetail currentLeaf = leafRepository.findById(leafId)
                .orElseThrow(() -> new RuntimeException("이파리는 존재하지 않습니다."));

        currentLeaf.setLeafComplain(currentLeaf.getLeafComplain() + 1);

        if (currentLeaf.getLeafComplain() >= 5) {
            // 삭제할 LeafDetail의 leaf_send 레코드 삭제
            leafSendRepository.deleteByLeafDetail(currentLeaf);

            // LeafDetail 삭제
            leafRepository.delete(currentLeaf);         }

        LeafComplaintResponse response = LeafComplaintResponse.createLeafComplaintResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<LeafViewResponse> view(Long views) {
        return null;

    }

}

