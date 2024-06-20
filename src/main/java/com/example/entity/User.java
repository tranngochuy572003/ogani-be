package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.UniqueKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

  @Column
  private String fullName ;
  @Column(unique = true, nullable = false)
  private String userName ;
  @Column(nullable = false)
  private String password ;
  @Column
  private String address;
  @Column
  private String phoneNumber;
  @Column
  private String role ;
  @Column
  private boolean isActive= true;

  @ManyToMany(mappedBy = "users")
  private Collection<Product> product;

  @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
  private List<Bill> bills;

  @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
  private List<News> news;

  @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, optional = false)
  private Cart cart;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
