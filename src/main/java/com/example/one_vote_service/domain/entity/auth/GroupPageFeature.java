package com.example.one_vote_service.domain.entity.auth;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class GroupPageFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pageId", referencedColumnName = "id")
    private Page page;

    @ManyToOne
    @JoinColumn(name = "groupId", referencedColumnName = "id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "featureId", referencedColumnName = "id")
    private Feature feature;

    private boolean statusPage;

    private boolean statusFeature;
}
