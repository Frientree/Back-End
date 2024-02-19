package com.d101.frientree.repository.user;

import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String username);

    Optional<User> findByNaverCode(String code);

    @Query("SELECT u FROM User u WHERE u.userFruitStatus = true AND u.userDisabled = false AND u.userNotification = true")
    List<User> findActiveUsersWithFruitStatusTrueAndNotificationTrue();


    //유저 열매 상태 수정 (clearAutomatically = true --> 영속성 컨텍스트 초기화 = 데이터 동기화)
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.userFruitStatus = :status WHERE u.userId = :userId")
    int updateUserFruitStatusById(@Param("userId") Long userId, @Param("status") Boolean status);

    //모든 유저 열매, 이파리 상태 수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.userFruitStatus = true, u.userLeafStatus = 3")
    void incrementAllUserFruitAndLeafStatus();


}
