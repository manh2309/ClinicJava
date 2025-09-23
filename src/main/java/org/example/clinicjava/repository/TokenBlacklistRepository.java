package org.example.clinicjava.repository;

import org.example.clinicjava.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlackList, Long> {
    Optional<TokenBlackList> findByToken(String token);
}
