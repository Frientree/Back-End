package com.d101.frientree.service.leaf;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.dto.leaf.response.LeafComplaintResponse;
import com.d101.frientree.dto.leaf.response.LeafConfirmationResponse;
import com.d101.frientree.dto.leaf.response.LeafGenerationResponse;
import com.d101.frientree.dto.leaf.response.LeafViewResponse;
import org.springframework.http.ResponseEntity;

public interface LeafService {

    // category 선택해서 조회하기
    ResponseEntity<LeafConfirmationResponse> confirm(int leafCategory);

    ResponseEntity<LeafGenerationResponse> generate(LeafGenerationRequest leafGenerationRequest, String createDate);

    ResponseEntity<LeafComplaintResponse> complain(Long leafId);

    ResponseEntity<LeafViewResponse> view();

    void moveAndDeleteOldLeaves();
}
