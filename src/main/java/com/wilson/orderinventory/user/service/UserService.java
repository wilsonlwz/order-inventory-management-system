package com.wilson.orderinventory.user.service;

import com.wilson.orderinventory.dto.LoginResponseDTO;
import com.wilson.orderinventory.jwt.JwtUtil;
import com.wilson.orderinventory.dto.LoginRequestDTO;
import com.wilson.orderinventory.dto.RegisterRequestDTO;
import com.wilson.orderinventory.token.RefreshToken;
import com.wilson.orderinventory.token.RefreshTokenResponseDTO;
import com.wilson.orderinventory.token.RefreshTokenService;
import com.wilson.orderinventory.user.User;
import com.wilson.orderinventory.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole("User");

        userRepository.save(user);
    }

    public RefreshTokenResponseDTO login(LoginRequestDTO requestDTO)
    {
        User user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new RefreshTokenResponseDTO(accessToken, refreshToken.getToken());
    }
}
