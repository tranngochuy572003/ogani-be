package com.example.ogani.controller;

import com.example.controller.UserController;
import com.example.dto.UserDto;
import com.example.exception.BadRequestException;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.example.common.MessageConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        userDto = new UserDto();
        userDto.setUserName("user");
        userDto.setFullName("user");
        userDto.setPassword("password");
        userDto.setAddress("address");
        userDto.setRole("ROLE_ADMIN");
    }

    @Test
    void testAddUserNotExistsByUsernameThenSuccess() throws Exception {
        doNothing().when(userService).addUser(any(UserDto.class));
        mockMvc.perform(post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_CREATED_SUCCESS));
    }

    @Test
    void testGetAllUserThenSuccess() throws Exception {
        UserDto user1 = new UserDto("user1@gmail.com", "user1", "abc123", "103 nct", "0905217612", "ROLE_USER", true);
        UserDto user2 = new UserDto("user2@gmail.com", "user2", "abc123", "109 nct", "0905217900", "ROLE_USER", true);

        List<UserDto> userDtoList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(userDtoList);

        mockMvc.perform(get("/api/v1/users/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data[0].userName").value("user1@gmail.com"))
                .andExpect(jsonPath("$.data[1].userName").value("user2@gmail.com"));
    }

    @Test
    void testGetUserByIdValidThenSuccess() throws Exception {
        when(userService.getUserById("1")).thenReturn(userDto);
        mockMvc.perform(get("/api/v1/users/get/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data.userName").value("user"))
                .andExpect(jsonPath("$.data.fullName").value("user"))
                .andExpect(jsonPath("$.data.address").value("address"));
    }

    @Test
    void testGetUserByIdInValidThenThrowBadRequest() throws Exception {
        when(userService.getUserById("1")).thenThrow(new BadRequestException(VALUE_NO_EXIST));
        mockMvc.perform(get("/api/v1/users/get/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(VALUE_NO_EXIST, result.getResolvedException().getMessage()));
    }

    @Test
    void testUpdateUserByIdValidThenSuccess() throws Exception {
        doNothing().when(userService).updateUser(any(String.class), any(UserDto.class));
        mockMvc.perform(patch("/api/v1/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_UPDATED_SUCCESS));
        verify(userService).updateUser(any(String.class), any(UserDto.class));
    }

    @Test
    void testUpdateUserByIdInValidThenThrowBadRequest() throws Exception {
        doThrow(new BadRequestException("Id is invalid")).when(userService).updateUser(any(String.class), any(UserDto.class));
        mockMvc.perform(patch("/api/v1/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("Id is invalid", result.getResolvedException().getMessage()));
        verify(userService).updateUser(any(String.class), any(UserDto.class));
    }

    @Test
    void testDeleteUserIdValidThenSuccess() throws Exception {
        doNothing().when(userService).deleteUser(any(String.class));
        mockMvc.perform(delete("/api/v1/users/delete/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_DELETED_SUCCESS));
        verify(userService).deleteUser(any(String.class));
    }

    @Test
    void testDeleteUserIdInValidThenThrowBadRequest() throws Exception {
        doThrow(new BadRequestException("Id is invalid")).when(userService).deleteUser(any(String.class));
        mockMvc.perform(delete("/api/v1/users/delete/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("Id is invalid", result.getResolvedException().getMessage()));
        verify(userService).deleteUser(any(String.class));
    }
}


