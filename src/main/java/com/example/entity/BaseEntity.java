package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
  @Id
  @UuidGenerator
  @Column(name ="id" , insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;

  @Column(name = "createdDate", updatable = false)
  @CreationTimestamp
  private LocalDateTime createdDate ;

  @Column(name="modifiedDate",updatable = false)
  @UpdateTimestamp
  private LocalDateTime modifiedDate ;

  @Column(name="createdBy")
  @CreatedBy
  private String createdBy ;

  @Column(name="modifiedBy")
  @LastModifiedBy
  private String modifiedBy ;




}
