package com.example.one_vote_service.domain.entity.auth;

import com.example.one_vote_service.config.constant.Channel;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String issuedAt;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Column(name = "is_logout")
    private boolean logout;

}
