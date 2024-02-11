package com.d101.frientree.repository.juice;

import com.d101.frientree.entity.juice.UserJuice;
import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserJuiceRepository extends JpaRepository<UserJuice, Long> {

    List<UserJuice> findAllByUser(User user);

    Optional<UserJuice> findByUser_UserIdAndUserJuiceCreateDate(Long userId, LocalDate createDate);
}
