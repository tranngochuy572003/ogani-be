package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Image extends BaseEntity{

  @Column
  private String urlImg ;

  @ManyToOne
  @JoinColumn(name="productId")
  private Product products;



}
