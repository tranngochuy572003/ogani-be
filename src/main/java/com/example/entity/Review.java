package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Review extends BaseEntity {
  @Column
  private Long rate ;

  @ManyToOne
  @JoinColumn(name = "productId")
  private Product products;





}
