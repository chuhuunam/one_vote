package com.example.one_vote_service.domain.dto.auth;

import lombok.Data;

import java.util.*;

@Data
public class DetailGroupDto {

    private Long id;

    private String code;

    private String name;

    private boolean statusPage;

    private List<DetailGroupDto> children = new ArrayList<>();

    private List<FeatureDto> features = new ArrayList<>();




}
