package com.example.one_vote_service.domain.request.auth;

import lombok.Data;

@Data
public class AddGroupRequest {
    private String code;
    private String name;
    private String description;
    private boolean status;
}
