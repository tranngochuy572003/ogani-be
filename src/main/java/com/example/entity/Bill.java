package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill extends BaseEntity {
  @Column
  private Double tax;
  @Column
  private Long totalPrice ;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User users;

}


