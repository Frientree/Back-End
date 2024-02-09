package com.d101.frientree.repository;

import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.leaf.LeafDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeafRepository extends JpaRepository<LeafDetail, Long> {
    Optional<LeafDetail> findTopByLeafCategoryOrderByLeafViewAsc(LeafCategory leafCategory);

    Optional<LeafDetail> findTopByLeafCategoryAndLeafNumNotInOrderByLeafViewAsc(
            LeafCategory leafCategory, List<Long> sentAndReceivedLeafNums);
}

