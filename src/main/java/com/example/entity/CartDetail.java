package com.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "cartDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail extends BaseEntity {
  @Column
  private boolean isChosen;
  @Column
  private Long quantityProduct;

  @ManyToOne
  @JoinColumn(name="productId")
   private Product products;

  @ManyToOne
  @JoinColumn(name="cartId")
  private Cart carts;


}

