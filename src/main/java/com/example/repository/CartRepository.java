package com.example.repository;

import com.example.entity.Cart;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartRepository  extends JpaRepository<Cart,String> {
    Cart findByUsers(User user);
    @Query("SELECT ca FROM carts ca WHERE ca.users.id = ?1 ")
    Cart findByUserId(String userId);
    @Modifying
    @Transactional
    @Query("DELETE FROM carts  ca WHERE ca.id = :cartId")
    void deleteCartById(String cartId);
}
