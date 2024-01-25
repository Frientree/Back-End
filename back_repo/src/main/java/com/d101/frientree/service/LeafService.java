package com.d101.frientree.service;

import com.d101.frientree.dto.leafdto.LeafCreateRequestDTO;
import com.d101.frientree.dto.leafdto.LeafCreateResponseDTO;
import com.d101.frientree.dto.leafdto.LeafReadResponseDTO;

public interface LeafService {

    // category 선택해서 조회하기
    LeafReadResponseDTO readByLeafCategory(String leafCategory);

    LeafCreateResponseDTO createLeaf(LeafCreateRequestDTO leafCreateRequestDTO);
}
