package com.d101.frientree.repository;

import com.d101.frientree.entity.fruit.FruitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FruitDetailRepository extends JpaRepository<FruitDetail, Long> {
    Optional<FruitDetail> findByFruitFeel(String feelText);
}
