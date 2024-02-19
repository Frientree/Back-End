package com.d101.frientree.repository.leaf;

import com.d101.frientree.entity.leaf.LeafCategory;
import com.d101.frientree.entity.leaf.LeafDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeafDetailRepository extends JpaRepository<LeafDetail, Long> {

    // join테이블 사용해서 userId 조회 후 보낸 이파리 노출횟수 조회하기
    @Query("SELECT SUM(ld.leafView) FROM LeafDetail ld JOIN ld.leafSends ls WHERE ls.user.userId = :userId")
    Long getTotalLeafViewByUserId(@Param("userId") Long userId);

    List<LeafDetail> findAllByLeafCreateDateBefore(LocalDate date);

    Optional<LeafDetail> findTopByLeafCategoryOrderByLeafViewAsc(String leafCategory);

    Optional<LeafDetail> findTopByLeafCategoryAndLeafNumNotInOrderByLeafViewAsc(
            String leafCategory, List<Long> sentAndReceivedLeafNums);
}