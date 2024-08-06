package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {


  @OneToMany(mappedBy = "carts", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartDetail> cartDetails;

  @OneToOne(optional = false)
  @JoinColumn(name = "userId")
  private User users;






}
