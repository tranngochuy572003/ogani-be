package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")

public class Category extends BaseEntity {

  @Column(unique = true,nullable = false)
  private String name ;
  @Column
  private String type ;
  @Column
  private boolean isActive= true;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private List<Product> products;
}
