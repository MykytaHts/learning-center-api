package com.example.learningcenterapi.security.repository;


import com.example.learningcenterapi.domain.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByTokenAndUserEmail(String token, String email);

    int deleteAllByUserId(Long userId);
}
