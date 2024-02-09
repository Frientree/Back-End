package com.d101.frientree.repository;

import com.d101.frientree.entity.app.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    // 필수 약관(true)을 먼저 정렬하고, 그 다음으로 terms_num으로 정렬
    @Query("SELECT t FROM Terms t ORDER BY CASE WHEN t.isNecessary = TRUE THEN 0 ELSE 1 END, t.termsNum")
    List<Terms> findAllOrderByTermsNecessaryDescAndTermsNumAsc();
}
