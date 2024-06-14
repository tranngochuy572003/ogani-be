package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Comment extends BaseEntity {
  private  String content ;

  @ManyToOne
  @JoinColumn(name="newsId")
  private News news ;


}
