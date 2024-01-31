package com.d101.frientree.repository;

import com.d101.frientree.entity.fruit.UserFruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFruitRepository extends JpaRepository<UserFruit, Long> {
}
