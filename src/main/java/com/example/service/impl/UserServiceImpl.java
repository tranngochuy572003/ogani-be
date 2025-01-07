package com.example.service.impl;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.BillService;
import com.example.service.UserService;
import com.example.util.AppUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.common.MessageConstant.*;


@Service
@Data
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private BillService billService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(UserDto userDto) {
        boolean existUser = userRepository.findUserByEmail(userDto.getUserName()) != null;
        if (existUser) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        if (userDto.getUserName().isEmpty()
                || userDto.getPassword().isEmpty()
                || userDto.getFullName().isEmpty()
                || userDto.getPhoneNumber().isEmpty()) {
            throw new BadRequestException(FIELD_REQUIRED);
        }
        if (AppUtil.containsSpecialCharacters(userDto.getFullName())) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!userDto.getUserName().endsWith(FORMAT_EMAIL)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        String namePart = userDto.getUserName().substring(0, userDto.getUserName().length() - "@gmail.com".length());
        if (!Pattern.matches("^[a-zA-Z0-9]+$", namePart)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (userDto.getPassword().chars().anyMatch(Character::isWhitespace)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!Pattern.compile("\\d+").matcher(userDto.getPhoneNumber()).matches()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!Pattern.compile("^[a-zA-Z0-9/\\s]+$").matcher(userDto.getAddress()).matches()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        User user = UserMapper.toCreateEntity(userDto);
        user.setRole(UserRole.CUSTOMER);
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
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }

    @Override
    public User findUserById(String id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        return user.get();
    }


    @Override
    public void deleteUser(String userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }

    @Override
    public void updateUser(String id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        if (optionalUser.isEmpty()) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        User user = optionalUser.get();
        if (userDto.getUserName().isEmpty()
                || userDto.getPassword().isEmpty()
                || userDto.getFullName().isEmpty()
                || userDto.getPhoneNumber().isEmpty()) {
            throw new BadRequestException(FIELD_REQUIRED);
        }
        if (AppUtil.containsSpecialCharacters(userDto.getFullName())) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!userDto.getUserName().endsWith(FORMAT_EMAIL)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        String namePart = userDto.getUserName().substring(0, userDto.getUserName().length() - FORMAT_EMAIL.length());
        if (!Pattern.matches("^[a-zA-Z0-9]+$", namePart)) {
            throw new BadRequestException(FIELD_INVALID);
        }

        if (userDto.getPassword().chars().anyMatch(Character::isWhitespace)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!Pattern.compile("\\d+").matcher(userDto.getPhoneNumber()).matches()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (!Pattern.compile("^[a-zA-Z0-9/\\s]+$").matcher(userDto.getAddress()).matches()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        boolean existUser = userRepository.findUserByEmail(userDto.getUserName()) != null;
        if (!userDto.getUserName().equals(user.getUsername()) && existUser) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        User userSaved = UserMapper.toUpdateEntity(user, userDto);
        userRepository.save(userSaved);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws BadRequestException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new BadRequestException(FIELD_INVALID);
        }
        return user;
    }

    @Override
    public User getUserByRefreshToken(String token) {
        return userRepository.findUserByRefreshToken(token);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public boolean isAuthorizedForCart(String cartId, String userId) {
        User user = userRepository.findByCartId(cartId);
        if (user == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        return Objects.equals(user.getId(), userId);
    }
}

