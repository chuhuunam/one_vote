package com.example.one_vote_service.domain.request.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    private String fcmKey;

    @NotEmpty(message = "Tài khoản không được để trống")
    private String username;

    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;

}
