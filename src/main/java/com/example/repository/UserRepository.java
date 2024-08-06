package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
  @Query("SELECT us FROM users  us WHERE us.userName = ?1 ")
  public User findUserByEmail(String email);

  @Query("SELECT us from users  us WHERE us.id=?1 ")
  public Optional<User> findUserById(String id);

  boolean existsByUserName(String username);
  User findUserByRefreshToken(String refreshToken);

  User findByCartId(String cartId);


}
