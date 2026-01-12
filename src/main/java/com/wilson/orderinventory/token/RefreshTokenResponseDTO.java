package com.wilson.orderinventory.token;

public class RefreshTokenResponseDTO {

    private String accessToken;
    private String refreshToken;

    public RefreshTokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
