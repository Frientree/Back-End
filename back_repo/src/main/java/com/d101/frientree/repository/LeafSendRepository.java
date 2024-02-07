package com.d101.frientree.repository;


import com.d101.frientree.entity.leaf.LeafDetail;
import com.d101.frientree.entity.leaf.LeafSend;
import com.d101.frientree.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface LeafSendRepository extends JpaRepository<LeafSend, Long> {
    void deleteByLeafDetail(LeafDetail leafDetail);

    List<LeafSend> findAllByUser(User user);

    @Query("SELECT ls.leafDetail.leafNum FROM LeafSend ls WHERE ls.user.userId = :userId")
    List<Long> findLeafNumsByUser(@Param("userId") Long userId);

    @Query("SELECT ls.leafDetail.leafNum FROM LeafSend ls WHERE ls.user.userId = :userId")
    List<Long> findSentLeafNumsByUserId(@Param("userId") Long userId);

}
