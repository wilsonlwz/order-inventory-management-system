package com.wilson.orderinventory.service;

import com.wilson.orderinventory.dto.LoginRequestDTO;
import com.wilson.orderinventory.jwt.JwtUtil;
import com.wilson.orderinventory.token.RefreshToken;
import com.wilson.orderinventory.token.RefreshTokenResponseDTO;
import com.wilson.orderinventory.token.RefreshTokenService;
import com.wilson.orderinventory.user.User;
import com.wilson.orderinventory.user.repository.UserRepository;
import com.wilson.orderinventory.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void login_success() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("admin", "1234");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("hashedPassword");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "hashedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken("admin"))
                .thenReturn("access-token");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        // Act
        RefreshTokenResponseDTO response = userService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void login_invalidPassword_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("admin", "wrong");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "hashedPassword"))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> userService.login(request));
    }

    @Test
    void login_userNotFound_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("abc", "1234");

        when(userRepository.findByUsername("abc"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.login(request));
    }
}
