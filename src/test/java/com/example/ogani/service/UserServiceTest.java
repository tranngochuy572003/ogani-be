package com.example.ogani.service;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.repository.UserRepository;
import com.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser =new User();
        mockUser.setUserName("userName@gmail.com");
        mockUser.setFullName("fullName");
        mockUser.setAddress("address");
        mockUser.setPhoneNumber("0917000749");
        mockUser.setId("userId");
        mockUser.setRole(UserRole.CUSTOMER);

    }

    private UserDto mockUserDto() {
        return new UserDto("userName@gmail.com", "fullName", "password", "address", "0917000749");
    }


    @Test
     void testAddUserNameNotExists() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.existsByUserName(mockUserDto.getUserName())).thenReturn(false);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        userService.addUser(mockUserDto);
        Assertions.assertEquals(mockUserDto.getUserName(), mockUser.getUsername());
        Assertions.assertEquals(mockUserDto.getFullName(), mockUser.getFullName());
        Assertions.assertEquals(mockUserDto.getAddress(), mockUser.getAddress());
        Assertions.assertEquals(mockUserDto.getPhoneNumber(), mockUser.getPhoneNumber());
    }

    @Test
     void testAddUserNameExistsThenSuccess() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.existsByUserName(mockUserDto.getUserName())).thenReturn(true);
        String userName = mockUserDto.getUserName();
        Assertions.assertThrows(BadRequestException.class, () -> userService.existsByUsername(userName));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
     void testGetAllUserThenSuccess() {
        User user1 = new User();
        user1.setUserName("userName1");
        user1.setRole(UserRole.CUSTOMER);
        User user2 = new User();
        user2.setUserName("userName2");
        user2.setRole(UserRole.ADMIN);
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> userDtoList = userService.getAllUsers();

        Assertions.assertEquals(userList.get(0).getUsername(), userDtoList.get(0).getUserName());
        Assertions.assertEquals(userList.get(1).getUsername(), userDtoList.get(1).getUserName());

    }


    @Test
     void testGetUserByIdValidThenSuccess() {
        when(userRepository.findUserById(any(String.class))).thenReturn(Optional.of(mockUser));
        UserDto userDto = userService.getUserById("userId");

        Assertions.assertEquals(userDto.getUserName(), mockUser.getUsername());
        Assertions.assertEquals(userDto.getFullName(), mockUser.getFullName());
        Assertions.assertEquals(userDto.getAddress(), mockUser.getAddress());
        Assertions.assertEquals(userDto.getPhoneNumber(), mockUser.getPhoneNumber());

    }

    @Test
     void testGetUserByIdInValidThenThrowBadRequest() {
        when(userRepository.findUserById(any(String.class))).thenReturn(Optional.empty());
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> userService.getUserById(null));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }

    @Test
     void testDeleteUserIdValidThenSuccess(){
        when(userRepository.findUserById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        userService.deleteUser(mockUser.getId());
        verify(userRepository).deleteById(mockUser.getId());
    }

    @Test
     void testDeleteUserIdInValidThenThrowBadRequest() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());
        verify(userRepository, never()).deleteById(mockUser.getId());
        Assertions.assertThrows(BadRequestException.class, () -> userService.deleteUser(null));
    }
}
