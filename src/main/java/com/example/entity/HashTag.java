package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity(name = "hashTags")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class HashTag {
  @Id
  @UuidGenerator
  @Column(name ="hashTagId" , insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;
  @Column
  private String hashTagName ;
  @ManyToMany(mappedBy = "hashTag")
  private Collection<News> news  ;
}
