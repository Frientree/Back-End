package com.d101.frientree.repository.message;

import com.d101.frientree.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}