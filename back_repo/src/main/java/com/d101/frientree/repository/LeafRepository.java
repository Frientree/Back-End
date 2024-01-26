package com.d101.frientree.repository;

import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.LeafDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeafRepository extends JpaRepository<LeafDetail, Long> {

    List<LeafDetail> findByLeafCategory(LeafCategory leafCategory);

}
