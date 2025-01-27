package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Product extends BaseEntity {
  @Column(unique = true,nullable = false)
  private String nameProduct ;
  @Column
  private boolean isActive;
  @Column
  private Long  inventory;
  @Column
  private String  description;
  @Column
  private String  information;
  @Column
  private Long price ;


  @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
  private List<Image> images;

  @ManyToOne
  @JoinColumn(name = "categoryProduct_id")
  private Category category;

  @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<CartDetail> cartDetails;
}
