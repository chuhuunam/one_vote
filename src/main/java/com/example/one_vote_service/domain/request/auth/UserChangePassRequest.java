package com.example.one_vote_service.domain.request.auth;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class UserChangePassRequest {

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Min(value = 6, message = "Password phải từ 6 kí tự trở lên")
    private String oldPassword;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Min(value = 6, message = "Password phải từ 6 kí tự trở lên")
    private String newPassword;

}
