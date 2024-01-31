package com.d101.frientree.repository;

import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.leaf.LeafReceive;
import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface LeafReceiveRepository extends JpaRepository<LeafReceive, Long> {
    List<LeafReceive> findByLeafDetail(LeafDetail currentLeaf);

    @Query("SELECT ls.leafDetail.leafNum FROM LeafReceive ls WHERE ls.user.userId = :userId")
    List<Long> findReceivedLeafNumsByUserId(@Param("userId") Long userId);

    boolean existsByUserAndLeafDetail(User user, LeafDetail selectedLeaf);
}