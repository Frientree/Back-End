package com.d101.frientree.repository.message;

import com.d101.frientree.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM message ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Message> findRandomMessage();
}