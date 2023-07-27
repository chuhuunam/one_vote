package com.example.one_vote_service.domain.entity.auth;

import com.example.one_vote_service.config.constant.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_fcm_keys")
@NoArgsConstructor
@AllArgsConstructor
public class UserFcmKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fcmKey;

    private String token;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    public UserFcmKey(Long userId, String fcmKey, Channel channel, String token) {
        this.userId = userId;
        this.fcmKey = fcmKey;
        this.channel = channel;
        this.token = token;
    }
}
