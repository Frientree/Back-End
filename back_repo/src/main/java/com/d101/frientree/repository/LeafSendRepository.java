package com.d101.frientree.repository;

import com.d101.frientree.entity.LeafDetail;
import com.d101.frientree.entity.LeafSend;
import com.d101.frientree.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeafSendRepository extends JpaRepository<LeafSend, Long> {
    void deleteByLeafDetail(LeafDetail leafDetail);


}
