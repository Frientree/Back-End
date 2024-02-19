package com.d101.frientree.repository.juice;

import com.d101.frientree.entity.juice.JuiceMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JuiceMessageRepository extends JpaRepository<JuiceMessage, Long> {

    @Query("SELECT s.juiceMessage FROM JuiceMessage s WHERE s.juiceDetail.juiceNum = :juiceNum ORDER BY RAND() LIMIT 1")
    String findRandomMessage(@Param("juiceNum") Long juiceNum);;
}
