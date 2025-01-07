package com.example.ogani.controller;

import com.example.controller.AuthController;
import com.example.dto.AuthenticationDto;
import com.example.dto.AuthorizationDto;
import com.example.dto.RegisterDto;
import com.example.dto.TokenDto;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.service.AuthService;
import com.example.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.example.common.MessageConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthenticationDto authenticationDto;
    private RegisterDto registerDto;
    private User user;
    @Mock
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        authenticationDto = new AuthenticationDto("helo@gmail.com", "abc123");
        registerDto = new RegisterDto("user1@gmail.com", "abc123", "abc123", "name", "address", "0917999213", "ROLE_USER");
        user = new User();
        user.setRole(UserRole.CUSTOMER);
        user.setId("id");
        user.setUserName("userName");
        user.setRefreshToken("refreshToken");
        user.setActive(true);
    }

    @Test
    void testLoginWhenAccountValidThenSuccess() throws Exception {
        String token = "token";
        String refreshToken = "refreshToken";
        List<String> roles = Arrays.asList(user.getRole().getValue());
        AuthorizationDto authorizationDto = new AuthorizationDto(token, refreshToken, user.getId(), user.getUsername(), user.isActive(), roles);
        Mockito.when(authService.login(authenticationDto)).thenReturn(authorizationDto);

        String authorizationDtoJson = objectMapper.writeValueAsString(authorizationDto);
        String expectedJson = "{ \"message\": \"Successfully\", \"data\": " + authorizationDtoJson + " }";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

    }

    @Test
    void testReFreshTokenThenSuccess() throws Exception {

        Mockito.when(jwtTokenService.createRefreshToken("refreshToken")).thenReturn("token");
        TokenDto tokenDto = new TokenDto("token");

        String tokenDtoJson = objectMapper.writeValueAsString(tokenDto);
        String expectedJson = "{ \"message\": \"Successfully\", \"data\": " + tokenDtoJson + " }";

        mockMvc.perform(post("/api/v1/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", user.getRefreshToken()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

    }

    @Test
    void testLoginWhenEmailInValidThenThrowBadRequest() throws Exception {
        Mockito.when(authService.login(authenticationDto)).thenThrow(new BadRequestException(FIELD_INVALID));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(FIELD_INVALID, result.getResolvedException().getMessage()));
    }

    @Test
    void testLoginWhenPassWordInValidThenThrowBadRequest() throws Exception {
        Mockito.when(authService.login(authenticationDto)).thenThrow(new BadRequestException(EMAIL_PASSWORD_INVALID));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(EMAIL_PASSWORD_INVALID, result.getResolvedException().getMessage()));
    }

    @Test
    void testRegisterWhenUserNameNotExistThenSuccess() throws Exception {
        authService.register(registerDto);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(REGISTER_SUCCESS));
    }


    @Test
    void testRegisterWhenUserNameExistedThenThrowBadRequest() throws Exception {
        doThrow(new BadRequestException("UserName existed")).when(authService).register(any(RegisterDto.class));
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("UserName existed", result.getResolvedException().getMessage()));
    }


    @Test
    void testLogoutThenSuccess() throws Exception {
        doNothing().when(authService).logout(user.getRefreshToken());
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", user.getRefreshToken()))
                .andExpect(jsonPath("$.message").value(LOGOUT_SUCCESS));
    }
}
