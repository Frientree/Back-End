package com.d101.frientree.repository;

import com.d101.frientree.entity.juice.UserJuice;
import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJuiceRepository extends JpaRepository<UserJuice, Long> {

    List<UserJuice> findAllByUser(User user);
}
