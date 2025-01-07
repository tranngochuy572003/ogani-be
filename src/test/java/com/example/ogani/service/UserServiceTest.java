package com.example.ogani.service;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        mockUser = new User();
        mockUser.setUserName("userName@gmail.com");
        mockUser.setFullName("fullName");
        mockUser.setAddress("address");
        mockUser.setPhoneNumber("0917000749");
        mockUser.setId("userId");
        mockUser.setRole(UserRole.CUSTOMER);

    }

    static Stream<Arguments> invalidUserInputs() {
        return Stream.of(
                Arguments.of("", "userNameEmpty"),
                Arguments.of("fullName//", "fullName"),
                Arguments.of("userName", "userNameInCorrectFormat"),
                Arguments.of("userName//@gmail.com", "userNameInValid"),
                Arguments.of("pas word", "password"),
                Arguments.of("0905a6817", "phone"),
                Arguments.of("103 nct\\", "address")

        );
    }

    private UserDto mockUserDto() {
        return new UserDto("userName@gmail.com", "fullName", "password", "address", "0917000749");
    }


    @Test
    void testAddUserWhenUserNameNotExists() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.findUserByEmail(mockUserDto.getUserName())).thenReturn(null);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        userService.addUser(mockUserDto);
        Assertions.assertEquals(mockUserDto.getUserName(), mockUser.getUsername());
        Assertions.assertEquals(mockUserDto.getFullName(), mockUser.getFullName());
        Assertions.assertEquals(mockUserDto.getAddress(), mockUser.getAddress());
        Assertions.assertEquals(mockUserDto.getPhoneNumber(), mockUser.getPhoneNumber());
    }

    @Test
    void testAddUserWhenUserNameExistedThenThenThrowBadRequest() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.findUserByEmail(mockUserDto.getUserName())).thenReturn(mockUser);
        Assertions.assertThrows(BadRequestException.class, () -> userService.addUser(mockUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("invalidUserInputs")
    void testAddUserWhenInputInvalidThenThrowBadRequest(String input, String field) {
        UserDto mockUserDto = mockUserDto();
        switch (field) {
            case "fullName" -> mockUserDto.setFullName(input);
            case "userNameInCorrectFormat", "userNameInValid", "userNameEmpty" -> mockUserDto.setUserName(input);
            case "password" -> mockUserDto.setPassword(input);
            case "phone" -> mockUserDto.setPhoneNumber(input);
            case "address" -> mockUserDto.setAddress(input);

            default -> throw new IllegalArgumentException("Unexpected field: " + field);
        }
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(BadRequestException.class, () -> userService.addUser(mockUserDto));
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
    void testDeleteUserIdValidThenSuccess() {
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

    @Test
    void testUpdateUserThenSuccess() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.findUserById("userId")).thenReturn(Optional.ofNullable(mockUser));
        when(userRepository.findUserByEmail(mockUserDto.getUserName())).thenReturn(null);
        userService.updateUser("userId", mockUserDto);
        Assertions.assertEquals(UserMapper.toUpdateEntity(mockUser, mockUserDto), mockUser);
    }


    @ParameterizedTest
    @MethodSource("invalidUserInputs")
    void testUpdateUserWhenInputInValidThenThrowBadRequest(String input, String field) {
        UserDto mockUserDto = mockUserDto();
        switch (field) {
            case "fullName" -> mockUserDto.setFullName(input);
            case "userNameInCorrectFormat", "userNameInValid", "userNameEmpty" -> mockUserDto.setUserName(input);
            case "password" -> mockUserDto.setPassword(input);
            case "phone" -> mockUserDto.setPhoneNumber(input);
            case "address" -> mockUserDto.setAddress(input);
            default -> throw new IllegalArgumentException("Unexpected field: " + field);
        }
        when(userRepository.findUserById(anyString())).thenReturn(Optional.of(mockUser));
        when(userRepository.findUserByEmail(mockUserDto.getUserName())).thenReturn(null);

        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser("userId", mockUserDto));
    }

    @Test
    void testUpdateUserWhenIdInvalidThenThrowBadRequest() {
        UserDto mockUserDto = mockUserDto();
        when(userRepository.findUserById("userId")).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser("userId", mockUserDto));
    }

    @Test
    void testUpdateUserWhenUserNameExistedThenThrowBadRequest() {
        UserDto mockUserDto = mockUserDto();
        User user = new User();
        user.setUserName("userName1@gmail.com");
        when(userRepository.findUserById("userId")).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail(mockUserDto.getUserName())).thenReturn(mockUser);
        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser("userId", mockUserDto));
    }

    @Test
    void testFindUserByIdThenSuccess() {
        when(userRepository.findUserById("userId")).thenReturn(Optional.of(mockUser));
        User user = userService.findUserById("userId");
        Assertions.assertEquals("userId", user.getId());
    }

    @Test
    void testFindUserByIdInvalidThenThrowBadRequest() {
        when(userRepository.findUserById("id")).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> userService.findUserById("id"));
    }

    @Test
    void testFindUserByEmailThenSuccess() {
        when(userRepository.findUserByEmail("userName@gmail.com")).thenReturn(mockUser);
        User user = userService.findUserByEmail("userName@gmail.com");
        Assertions.assertEquals(mockUser, user);
    }
    @Test
    void testLoadUserByUsernameThenSuccess() {
        when(userRepository.findUserByEmail("userName@gmail.com")).thenReturn(mockUser);
        UserDetails userDetails = userService.loadUserByUsername("userName@gmail.com");
        Assertions.assertEquals(mockUser, userDetails);
    }
    @Test
    void testGetUserByRefreshToken() {
        userService.getUserByRefreshToken(anyString());
        verify(userRepository).findUserByRefreshToken(anyString());
    }
    @Test
    void testSaveUser() {
        userService.save(mockUser);
        verify(userRepository).save(mockUser);
    }

    @Test
    void testIsAuthorizedForCartThenSuccess() {
        when(userRepository.findByCartId("cartId")).thenReturn(mockUser);
        Assertions.assertTrue(userService.isAuthorizedForCart("cartId","userId"));
    }

    @Test
    void testIsAuthorizedForCartWhenUserIdInvalidThenThrowBadRequest() {
        when(userRepository.findByCartId("cartId")).thenReturn(null);
        Assertions.assertThrows(BadRequestException.class, () -> userService.isAuthorizedForCart("cartId","userId"));
    }
}

