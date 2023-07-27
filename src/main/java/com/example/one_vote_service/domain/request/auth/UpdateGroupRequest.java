package com.example.one_vote_service.domain.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class UpdateGroupRequest {
    private String code;
    private String name;
    private String description;
    private boolean status;
    private List<PageRoleDto> pageFeatures;


    @Data
    @AllArgsConstructor
    public class PageRoleDto {

    }

    @Data
    @AllArgsConstructor
    public class PageUpdateDto {
        private Long pageId;

    }

    @Data
    @AllArgsConstructor
    public class FeatureUpdateDto {

    }

}
