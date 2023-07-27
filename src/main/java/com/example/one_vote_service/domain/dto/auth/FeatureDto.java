package com.example.one_vote_service.domain.dto.auth;

import com.example.one_vote_service.domain.entity.auth.Feature;
import lombok.Data;

@Data
public class FeatureDto {
    private Long id;

    private String code;

    private String name;

    private Long pageId;

    private boolean statusFeature;

    public FeatureDto(Feature feature) {
        this.id = feature.getId();
        this.code = feature.getCode();
        this.name = feature.getName();
        this.pageId = feature.getPage().getId();
    }

    public FeatureDto(Feature feature, boolean statusFeature) {
        this.id = feature.getId();
        this.code = feature.getCode();
        this.name = feature.getName();
        this.name = feature.getName();
        this.statusFeature = statusFeature;
        this.pageId = feature.getPage().getId();
    }
}
