package com.d101.frientree.repository.juice;

import com.d101.frientree.entity.juice.JuiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JuiceDetailRepository extends JpaRepository<JuiceDetail, Long> {

    @Query("SELECT jd FROM JuiceDetail jd WHERE :score >= jd.juiceMinScore AND :score <= jd.juiceMaxScore")
    JuiceDetail findJuicesByScore(@Param("score") Long score);
}
