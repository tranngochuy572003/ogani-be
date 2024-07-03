package com.example.repository;

import com.example.entity.Cart;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<Cart,String> {
    Cart findByUsers(User user);
}
