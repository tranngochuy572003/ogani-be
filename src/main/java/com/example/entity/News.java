package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class News extends BaseEntity {

  @Column(nullable = false)
  private String title;
  @Column
  private String content;

  @ManyToOne
  @JoinColumn(name = "categoryBlog_id")
  private Category category;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
          name = "hashtags_news",
          joinColumns = @JoinColumn(name = "newId", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "hashtagId", referencedColumnName = "hashTagId")
  )
  private Collection<HashTag> hashTag ;


  @OneToMany(mappedBy = "news")
  private List<Comment> comments ;

  @ManyToOne
  @JoinColumn(name ="userId")
  private User users;
}
