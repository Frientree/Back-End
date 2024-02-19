package com.d101.frientree.repository.certification;

import com.d101.frientree.entity.certification.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
