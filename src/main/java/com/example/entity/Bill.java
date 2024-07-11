package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill extends BaseEntity {
  @Column
  private Double tax;
  @Column
  private Long totalPrice ;
  @Column
  private boolean isConfirm ;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User users;
  @OneToMany(mappedBy = "bills", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BillDetail> billDetailList;

}


