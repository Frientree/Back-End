package com.d101.frientree.repository;

import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.LeafDetail;
import com.d101.frientree.entity.LeafReceive;
import com.d101.frientree.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeafRepository extends JpaRepository<LeafDetail, Long> {

    List<LeafDetail> findByLeafCategory(LeafCategory leafCategory);

    Optional<LeafDetail> findById(Long id);

}
