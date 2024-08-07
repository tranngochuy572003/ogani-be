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
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    private JWT jwtMock;
    private JWTClaimsSet claimsSetMock;


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
        jwtMock = mock(JWT.class);
        claimsSetMock = mock(JWTClaimsSet.class);

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
    void testLoginSuccess() throws Exception {
        AuthenticationDto authenticationDto = new AuthenticationDto("userName", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        when(userService.findUserByEmail("userName")).thenReturn(user);
        when(passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword())).thenReturn(true);

        try (MockedStatic<JWTParser> jwtParserMock = mockStatic(JWTParser.class)) {
            jwtParserMock.when(() -> JWTParser.parse(anyString())).thenReturn(jwtMock);
            when(jwtMock.getJWTClaimsSet()).thenReturn(claimsSetMock);
            when(claimsSetMock.getClaim("userName")).thenReturn("userName");

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
    }

    @Test
    void testRegisterUserNameNotExistThenSuccess() {
        when(userService.existsByUsername(registerDto.getUserName())).thenReturn(true);
        UserDto userDto = UserMapper.toUserDto(registerDto);
        userDto.setActive(true);
        authService.register(registerDto);
        verify(userService).addUser(userDto);
    }


    @Test
    void registerWhenUserNameExistedThenThrowBadRequest() {
        when(userService.existsByUsername(registerDto.getUserName())).thenThrow(new BadRequestException("UserName existed"));
        assertThrows(BadRequestException.class, () -> authService.register(registerDto));
        UserDto userDto = UserMapper.toUserDto(registerDto);
        verify(userService, never()).addUser(userDto);
    }

}
