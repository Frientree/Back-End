package com.d101.frientree.repository;

import com.d101.frientree.entity.leaf.LeafReceive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeafReceiveRepository extends JpaRepository<LeafReceive, Long> {
    List<LeafReceive> findByUserId(Long userId);

}
