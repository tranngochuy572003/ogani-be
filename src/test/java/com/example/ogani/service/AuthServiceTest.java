package com.example.ogani.service;

import com.example.dto.AuthenticationDto;
import com.example.dto.AuthorizationDto;
import com.example.dto.RegisterDto;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.service.impl.AuthServiceImpl;
import com.example.service.impl.JwtTokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenServiceImpl jwtTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;


    private String refreshToken;
    private String token;

    private RegisterDto registerDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshToken = "eyJhbGciXVCJ9.eyJyb2xlIEyODkwMFkIn0.sC66PFwnHWyjHrb";
        user = new User();
        user.setActive(true);
        user.setRefreshToken("refreshToken");
        token = "eyJhbGciOXVCJ9.eyJyb2xlIEyODkwMDFkIn0.sC66PjFwnHWyjHrb";
        registerDto = new RegisterDto("userName", "password", "password", "fullName", "address", "phoneNumber", "ROLE_USER");

    }

    @Test
    void testLogoutThenSuccess() {
        when(jwtTokenService.verifyExpiration(anyString())).thenReturn(true);
        when(userService.getUserByRefreshToken(anyString())).thenReturn(user);
        authService.logout(refreshToken);

        assertNull(user.getRefreshToken());
        verify(userService).save(user);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationDto authenticationDto = new AuthenticationDto("userName", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        when(userService.findUserByEmail("userName")).thenReturn(user);
        when(passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenService.createToken(authenticationDto.getUserName())).thenReturn(token);
        when(jwtTokenService.createRefreshToken(token)).thenReturn(refreshToken);

        AuthorizationDto result = authService.login(authenticationDto);

        assertNotNull(result);
        assertEquals(token, result.getToken().getAccessToken());
        assertEquals(refreshToken, result.getToken().getRefreshToken());

        verify(userService).loadUserByUsername(authenticationDto.getUserName());
        verify(passwordEncoder).matches(authenticationDto.getPassword(), user.getPassword());
        verify(jwtTokenService).createToken(authenticationDto.getUserName());
        verify(jwtTokenService).createRefreshToken(token);

    }


    @Test
    void testLoginWhenUserInActiveThenThrowBadRequest() {
        AuthenticationDto authenticationDto = new AuthenticationDto("userName", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        when(passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword())).thenReturn(true);

        user.setActive(false);
        when(userService.findUserByEmail("userName")).thenReturn(user);
        when(jwtTokenService.createToken(authenticationDto.getUserName())).thenReturn(token);
        when(jwtTokenService.createRefreshToken(token)).thenReturn(refreshToken);
        Assertions.assertThrows(BadRequestException.class, () -> authService.login(authenticationDto));

    }
    @Test
    void testLoginWhenUserEnterInvalidEmailThenThrowBadRequest() {
        AuthenticationDto authenticationDto = new AuthenticationDto("userNameInvalid", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        Assertions.assertThrows(BadRequestException.class, () -> authService.login(authenticationDto));
    }

    @Test
    void testLoginWhenUserEnterInvalidPasswordThenThrowBadRequest() {
        AuthenticationDto authenticationDto = new AuthenticationDto("userName", "passwordInvalid");
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        Assertions.assertThrows(BadRequestException.class, () -> authService.login(authenticationDto));
    }


    @Test
    void testLogoutInvalidTokenThenThrowBadRequest() {
        when(jwtTokenService.verifyExpiration(token)).thenReturn(true);
        when(userService.getUserByRefreshToken(token)).thenReturn(null);
        assertThrows(BadRequestException.class, () -> authService.logout(token));
        verify(jwtTokenService).verifyExpiration(token);
        verify(userService).getUserByRefreshToken(token);
    }

    @Test
    void testRegisterUserNameNotExistThenSuccess() {
        when(userService.findUserByEmail(registerDto.getUserName())).thenReturn(user);
        UserDto userDto = UserMapper.toUserDto(registerDto);
        userDto.setActive(true);
        authService.register(registerDto);
        verify(userService).addUser(userDto);
    }


    @Test
    void registerWhenUserNameExistedThenThrowBadRequest() {
        when(userService.findUserByEmail(registerDto.getUserName()) != null).thenThrow(new BadRequestException("UserName existed"));
        assertThrows(BadRequestException.class, () -> authService.register(registerDto));
        UserDto userDto = UserMapper.toUserDto(registerDto);
        verify(userService, never()).addUser(userDto);
    }
    @Test
    void registerWhenPasswordDifferentConfirmPasswordThenThrowBadRequest() {
        registerDto.setConfirmPassword("otherPassword");
        when(userService.findUserByEmail(registerDto.getUserName())).thenReturn(user);
        assertThrows(BadRequestException.class, () -> authService.register(registerDto));
    }
}
