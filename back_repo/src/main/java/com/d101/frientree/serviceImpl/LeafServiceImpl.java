package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.leaf.response.dto.LeafCreateRequestDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafCreateResponseDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafReadResponseDTO;
import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.LeafDetail;
import com.d101.frientree.repository.LeafRepository;
import com.d101.frientree.service.LeafService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public LeafReadResponseDTO readByLeafCategory(String leafCategory) {
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

            // 선택된 leaf를 LeafReadResponseDTO로 변환하여 반환
            return LeafReadResponseDTO.builder()
                    .leafNum(selectedLeaf.getLeafNum())
                    .leafContent(selectedLeaf.getLeafContent())
                    .leafCategory(selectedLeaf.getLeafCategory())
                    .build();
        }

        // 찾는 leaf가 없는 경우 null 반환
        return null;
//   이파리가 없는 경우(서비스초기) default leaf -> 예비로 넣어놓기 처리할 것
//   default leaf 를 던져줄 수 있게 처리...
    }


    @Override
    public LeafCreateResponseDTO createLeaf(LeafCreateRequestDTO leafCreateRequestDTO) {

        LocalDateTime leafCreateDate = LocalDateTime.now();

        LeafDetail newLeaf = LeafDetail.builder()
                .leafCategory(leafCreateRequestDTO.getLeafCategory())
                .leafContent(leafCreateRequestDTO.getLeafContent())
                .leafCreateDate(Date.from(leafCreateDate.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        leafRepository.save(newLeaf);

        return LeafCreateResponseDTO.builder()
                .isCreated(true)
                .build();
    }
}


