package com.d101.frientree.repository;

import com.d101.frientree.entity.leaf.LeafDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeafDetailRepository extends JpaRepository<LeafDetail, Long> {
    @Query("SELECT SUM(ld.leafView) FROM LeafDetail ld WHERE ld = :leafDetail")
    Long sumLeafViewByLeafDetail(@Param("leafDetail") LeafDetail leafDetail);

    LeafDetail findByLeafNum(Long leafNum);
}