package com.d101.frientree.repository;

import com.d101.frientree.entity.fruit.UserFruit;
import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public interface UserFruitRepository extends JpaRepository<UserFruit, Long> {
    List<UserFruit> findAllByUserAndUserFruitCreateDateBetween(User user, Date startDate, Date endDate);
}
