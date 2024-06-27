package com.example.repository;

import com.example.entity.RefreshToken;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByToken(String token);
    @Query("SELECT rt.user FROM refreshtoken rt WHERE rt.token = ?1 ")
    User findUserByToken(String token);
}
