package com.example.repository;

import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartDetailRepository  extends JpaRepository<CartDetail,String> {
    List<CartDetail> findByCarts(Cart cart);
    CartDetail findByProducts(Product product);
    @Modifying
    @Transactional
    @Query("DELETE FROM cartDetails  cd WHERE cd.carts = :cart")
    void deleteAllByCarts(Cart cart);
}
