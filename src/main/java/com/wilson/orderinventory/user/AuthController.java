package com.wilson.orderinventory.user;

import com.wilson.orderinventory.dto.LoginRequestDTO;
import com.wilson.orderinventory.dto.LoginResponseDTO;
import com.wilson.orderinventory.dto.LogoutRequestDTO;
import com.wilson.orderinventory.dto.RegisterRequestDTO;
import com.wilson.orderinventory.token.RefreshTokenRepository;
import com.wilson.orderinventory.token.RefreshTokenResponseDTO;
import com.wilson.orderinventory.token.RefreshTokenService;
import com.wilson.orderinventory.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(UserService userService, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO requestDTO)
    {
        userService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<RefreshTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO)
    {
        RefreshTokenResponseDTO response = userService.login(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody LogoutRequestDTO requestDTO) {

        refreshTokenRepository.findByToken(requestDTO.getRefreshToken()).ifPresent(refreshTokenRepository::delete);
        return ResponseEntity.ok("Logged out successfully");
    }

}
