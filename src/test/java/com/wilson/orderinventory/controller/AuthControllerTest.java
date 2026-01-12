package com.wilson.orderinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.orderinventory.dto.LoginRequestDTO;
import com.wilson.orderinventory.token.RefreshTokenResponseDTO;
import com.wilson.orderinventory.user.AuthController;
import com.wilson.orderinventory.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_success_returnsToken() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("admin", "1234");

        RefreshTokenResponseDTO serviceResponse =
                new RefreshTokenResponseDTO("access-token-123", "refresh-token-456");

        when(userService.login(any(LoginRequestDTO.class)))
                .thenReturn(serviceResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-456"));
    }

    @Test
    void login_invalidCredentials_returns400() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("admin", "wrong");

        when(userService.login(any(LoginRequestDTO.class)))
                .thenThrow(new RuntimeException("Invalid username or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void login_blankUsername_returns400() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}

