package com.example.one_vote_service.domain.request.auth;

import lombok.Data;

@Data
public class MailForgetPassRequest {
    private String to;
    private String fullName;
    private String password;
}
