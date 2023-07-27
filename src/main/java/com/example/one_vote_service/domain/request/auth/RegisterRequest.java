package com.example.one_vote_service.domain.request.auth;

import com.example.one_vote_service.config.constant.Gender;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class RegisterRequest {

    private String avatar;

    @NotEmpty(message = "Tài khoản không được để trống")
    private String username;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Min(value = 6, message = "Password phải từ 6 kí tự trở lên")
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    private Date birthday;

    private boolean status;

}
