package com.example.one_vote_service.domain.request.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenRequest {

    private String fcmKey;

    @NotEmpty(message = "Refresh Token không được để trống")
    private String refreshToken;
}
