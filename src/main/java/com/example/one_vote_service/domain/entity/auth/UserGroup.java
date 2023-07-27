package com.example.one_vote_service.domain.entity.auth;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "groupId", referencedColumnName = "id")
    private Group group;
}
