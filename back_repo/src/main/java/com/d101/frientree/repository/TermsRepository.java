package com.d101.frientree.repository;

import com.d101.frientree.entity.app.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    
}
