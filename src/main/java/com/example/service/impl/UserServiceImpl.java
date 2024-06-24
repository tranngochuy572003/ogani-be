package com.example.service.impl;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Data
public class UserServiceImpl implements UserService, UserDetailsService {
  private UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void addUser(UserDto userDto) {
    if (userDto.getPassword().isEmpty() || userDto.getUserName().isEmpty() || userDto.getFullName().isEmpty()) {
      throw new BadRequestException("Field required is not blank.");
    }
    User user =UserMapper.toCreateEntity(userDto);
    userRepository.save(user);
  }

  @Override
  public List<UserDto> getAllUsers() {
    return UserMapper.toListDto(userRepository.findAll());
  }

  @Override
  public UserDto getUserById(String id) {
    Optional<User> user = userRepository.findUserById(id);
    if (user.isPresent()) {
      return UserMapper.toDto(user.get());
    } else {
      throw new com.example.exception.BadRequestException("Email or Password is invalid");
    }
  }



  @Override
  public void deleteUser(String userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      userRepository.deleteById(userId);
    } else {
      throw new com.example.exception.BadRequestException("Id is invalid");
    }
  }

  @Override
  public void updateUser(String id, UserDto userDto) {
    Optional<User> optionalUser = userRepository.findUserById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      User userSaved = UserMapper.toUpdateEntity(user, userDto);
      userRepository.save(userSaved);
    } else {
      throw new com.example.exception.BadRequestException("Id is invalid");
    }
  }

  @Override
  public User findUserByEmail(String email) {
    User user = userRepository.findUserByEmail(email);
    if (user == null) {
      throw new com.example.exception.BadRequestException("Email is invalid");
    }
    return user;
  }


  @Override
  public boolean existsByUsername(String username) {
    if (userRepository.existsByUserName(username)) {
      throw new BadRequestException("UserName existed");
    }
    return true;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }
    return user;
  }



}

