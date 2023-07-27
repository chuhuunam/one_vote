package com.example.one_vote_service.domain.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String icon;

    private String url;

    private String pageIndex;

    private Long parentId;

    @OneToMany(mappedBy = "page", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feature> features = new ArrayList<>();

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GroupPageFeature> groupPageFeatures = new ArrayList<>();

    private boolean status;

}
