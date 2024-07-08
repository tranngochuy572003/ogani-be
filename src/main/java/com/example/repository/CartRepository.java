package com.example.repository;

import com.example.entity.Cart;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<Cart,String> {
    Cart findByUsers(User user);
    @Query("SELECT ca FROM carts ca WHERE ca.users.id = ?1 ")
    Cart findByUserId(String userId);
}
