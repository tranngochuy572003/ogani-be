package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Contact {
  @Id
  @UuidGenerator
  @Column(name ="id" , insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;
  @Column
  private String email;
  @Column
  private String address;
  @Column
  private String phone;
  @Column
  private String openTime;



}
