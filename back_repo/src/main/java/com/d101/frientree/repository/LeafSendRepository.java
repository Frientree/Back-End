package com.d101.frientree.repository;


import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.leaf.LeafSend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeafSendRepository extends JpaRepository<LeafSend, Long> {
    void deleteByLeafDetail(LeafDetail leafDetail);


}
