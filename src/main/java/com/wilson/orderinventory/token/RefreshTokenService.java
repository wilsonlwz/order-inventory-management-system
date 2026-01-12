package com.wilson.orderinventory.token;

import com.wilson.orderinventory.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository repo)
    {
        this.refreshTokenRepository = repo;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user)
    {
        refreshTokenRepository.deleteByUser(user); // one active token per user

        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (refreshTokenRepository.findByToken(token).isPresent());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plus(REFRESH_TOKEN_DURATION_DAYS, ChronoUnit.DAYS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token)
    {
        if (token.getExpiryDate().isBefore(Instant.now()))
        {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    public void deleteByToken(String token)
    {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteByUser(User user)
    {
        refreshTokenRepository.deleteByUser(user);
    }
}

