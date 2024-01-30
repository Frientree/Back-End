package com.d101.frientree.repository;

import com.d101.frientree.entity.leaf.LeafReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeafReceiveRepository extends JpaRepository<LeafReceive, Long> {
    @Query("SELECT lr FROM LeafReceive lr WHERE lr.user.userId = :userId")
    List<LeafReceive> findByUserId(@Param("userId") Long userId);
}