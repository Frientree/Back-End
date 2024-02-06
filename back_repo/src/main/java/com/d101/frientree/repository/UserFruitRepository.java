package com.d101.frientree.repository;

import com.d101.frientree.entity.fruit.UserFruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserFruitRepository extends JpaRepository<UserFruit, Long> {

    List<UserFruit> findAllByUser_UserIdAndUserFruitCreateDateBetweenOrderByUserFruitCreateDateAsc(Long userId, Date startDate, Date endDate);

    Optional<UserFruit> findByUser_UserIdAndUserFruitCreateDate(Long userId, Date toDay);

    @Query("SELECT COUNT(uf) FROM UserFruit uf WHERE uf.fruitDetail.fruitNum = :fruitNum AND uf.userFruitCreateDate = :date AND uf.user.userId != :userId")
    Long countByFruitNumAndDateExcludingUser(@Param("fruitNum") Long fruitNum, @Param("date") Date date, @Param("userId") Long userId);

}
