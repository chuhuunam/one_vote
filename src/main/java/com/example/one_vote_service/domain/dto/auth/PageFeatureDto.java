package com.example.one_vote_service.domain.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class PageFeatureDto {

    private List<PageDto> pages;
    private List<String> features;
}
