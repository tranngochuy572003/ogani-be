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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserDto mockUserDto() {
        return new UserDto("userName", "fullName", "password", "address", "phoneNumber");
    }

    private User mockUser() {
        return new User("1", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName", "userName", "password", "address", "phoneNumber", UserRole.CUSTOMER, false, null, null, null, null, null);
    }

    @Test
    public void testAddUserNameNotExists() {
        User mockUser = mockUser();
        UserDto mockUserDto = mockUserDto();
        userService.addUser(mockUserDto);
        Mockito.when(userRepository.existsByUserName(mockUserDto.getUserName())).thenReturn(false);
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);
        Assertions.assertEquals(mockUserDto.getUserName(), mockUser.getUsername());
        Assertions.assertEquals(mockUserDto.getFullName(), mockUser.getFullName());
        Assertions.assertEquals(mockUserDto.getAddress(), mockUser.getAddress());
        Assertions.assertEquals(mockUserDto.getPhoneNumber(), mockUser.getPhoneNumber());
        Assertions.assertEquals(mockUser.getRole().getValue(), "ROLE_CUSTOMER");
    }

    @Test
    public void testAddUserNameExistsThenSuccess() {
        UserDto mockUserDto = mockUserDto();
        Mockito.when(userRepository.existsByUserName(mockUserDto.getUserName())).thenReturn(true);
        Assertions.assertThrows(BadRequestException.class, () -> userService.existsByUsername(mockUserDto.getUserName()));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUserThenSuccess() {
        User user1 = new User("1", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName1", "userName1", "password1", "address1", "phoneNumber1", UserRole.CUSTOMER, false, null, null, null, null, null);
        User user2 = new User("2", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName2", "userName2", "password2", "address2", "phoneNumber2", UserRole.CUSTOMER, false, null, null, null, null, null);

        UserDto userDto1 = new UserDto("userName1", "fullName1", "password1", "address1", "phoneNumber1");
        UserDto userDto2 = new UserDto("userName2", "fullName2", "password2", "address2", "phoneNumber2");

        List<User> userList = Arrays.asList(user1, user2);
        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userService.getAllUsers()).thenReturn(userDtoList);
        Assertions.assertEquals(userList.get(0).getUsername(), userDtoList.get(0).getUserName());
        Assertions.assertEquals(userList.get(1).getUsername(), userDtoList.get(1).getUserName());
        Assertions.assertEquals(userList.get(0).getFullName(), userDtoList.get(0).getFullName());
        Assertions.assertEquals(userList.get(1).getFullName(), userDtoList.get(1).getFullName());
    }


    @Test
    public void testGetUserByIdValidThenSuccess() {
        Mockito.when(userRepository.findUserById(any(String.class))).thenReturn(Optional.of(mockUser()));
        UserDto userDto = userService.getUserById("1");

        Assertions.assertEquals(userDto.getUserName(), mockUser().getUsername());
        Assertions.assertEquals(userDto.getFullName(), mockUser().getFullName());
        Assertions.assertEquals(userDto.getAddress(), mockUser().getAddress());
        Assertions.assertEquals(userDto.getPhoneNumber(), mockUser().getPhoneNumber());

    }

    @Test
    public void testGetUserByIdInValidThenThrowBadRequest() {
        Mockito.when(userRepository.findUserById(any(String.class))).thenReturn(Optional.empty());
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> userService.getUserById(any(String.class)));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }

    @Test
    public void testDeleteUserIdValidThenSuccess(){
        when(userRepository.findById(mockUser().getId())).thenReturn(Optional.of(mockUser()));
        userService.deleteUser(mockUser().getId());
        verify(userRepository, times(1)).deleteById(mockUser().getId());
    }

    @Test
    public void testDeleteUserIdInValidThenThrowBadRequest() {
        when(userRepository.findById(mockUser().getId())).thenReturn(Optional.empty());
        verify(userRepository, never()).deleteById(mockUser().getId());
        Assertions.assertThrows(BadRequestException.class, () -> userService.deleteUser(any(String.class)));
    }
}
