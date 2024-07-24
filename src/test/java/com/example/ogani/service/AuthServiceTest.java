package com.example.ogani.service;

import com.example.dto.AuthorizationDto;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.service.impl.AuthServiceImpl;
import com.example.service.impl.JwtTokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AuthServiceTest {

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

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthenticationDto authenticationDto;
    private String refreshToken;
    private String token;

    private RegisterDto registerDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authService).build();
        objectMapper = new ObjectMapper();
        authenticationDto = new AuthenticationDto("userName", "password");
        refreshToken = "eyJhbGciXVCJ9.eyJyb2xlIEyODkwMFkIn0.sC66PFwnHWyjHrb";
        user = new User("1", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName", "userName", "password", "address", "phoneNumber", UserRole.CUSTOMER, false, refreshToken, null, null, null);
        token = "eyJhbGciOXVCJ9.eyJyb2xlIEyODkwMDFkIn0.sC66PjFwnHWyjHrb";
        registerDto = new RegisterDto("userName", "password", "password", "fullName", "address", "phoneNumber", "ROLE_USER");
        jwtMock = mock(JWT.class);
        claimsSetMock = mock(JWTClaimsSet.class);

    }

    @Test
    public void testLogoutThenSuccess() {
        Mockito.when(jwtTokenService.verifyExpiration(anyString())).thenReturn(true);
        Mockito.when(userService.getUserByRefreshToken(user.getRefreshToken())).thenReturn(user);
        authService.logout(refreshToken);

        Assertions.assertNull(user.getRefreshToken());
        verify(userService, times(1)).save(user);
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthenticationDto authenticationDto = new AuthenticationDto("userName", "password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authenticationDto.getUserName())).thenReturn(userDetails);
        when(userService.findUserByEmail("userName")).thenReturn(user);
        when(passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword())).thenReturn(true);


        try (MockedStatic<JWTParser> jwtParserMock = Mockito.mockStatic(JWTParser.class)) {
            jwtParserMock.when(() -> JWTParser.parse(anyString())).thenReturn(jwtMock);
            when(jwtMock.getJWTClaimsSet()).thenReturn(claimsSetMock);
            when(claimsSetMock.getClaim("userName")).thenReturn("userName");

            when(jwtTokenService.createToken(authenticationDto.getUserName())).thenReturn(token);
            when(jwtTokenService.createRefreshToken(token)).thenReturn(refreshToken);

            AuthorizationDto result = authService.login(authenticationDto);

            assertNotNull(result);
            assertEquals(token, result.getToken().getAccessToken());
            assertEquals(refreshToken, result.getToken().getRefreshToken());

            verify(userService, times(1)).loadUserByUsername(authenticationDto.getUserName());
            verify(passwordEncoder, times(1)).matches(authenticationDto.getPassword(), user.getPassword());
            verify(jwtTokenService, times(1)).createToken(authenticationDto.getUserName());
            verify(jwtTokenService, times(1)).createRefreshToken(token);
        }
    }

    @Test
    void testRegisterUserNameNotExistThenSuccess(){
        when(userService.existsByUsername(registerDto.getUserName())).thenReturn(true);
        UserDto userDto = UserMapper.toUserDto(registerDto);
        authService.register(registerDto);
        verify(userService, times(1)).addUser(userDto);
    }


    @Test
    void registerWhenUserNameExistedThenThrowBadRequest() {
        when(userService.existsByUsername(registerDto.getUserName())).thenThrow(new BadRequestException("UserName existed"));
        assertThrows(BadRequestException.class, () -> authService.register(registerDto));

        UserDto userDto = UserMapper.toUserDto(registerDto);
        verify(userService, never()).addUser(userDto);
    }

}
