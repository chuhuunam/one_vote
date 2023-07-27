package com.example.one_vote_service.domain.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "features")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pageId", insertable = false, updatable = false)
    private Page page;

    @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GroupPageFeature> groupPageFeatures = new ArrayList<>();
}
