package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {


  @OneToMany(mappedBy = "carts", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<CartDetail> cartDetails;

  @OneToOne(optional = false)
  @JoinColumn(name = "userId")
  private User users;






}
