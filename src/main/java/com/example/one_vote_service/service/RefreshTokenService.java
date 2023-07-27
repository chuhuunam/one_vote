package com.example.one_vote_service.service;

import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.domain.entity.auth.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId, Channel channel);

    Optional<RefreshToken> findByToken(String requestRefreshToken);

    RefreshToken verifyExpiration(RefreshToken refreshToken);

    void deleteByUserId(Long userId, Channel channel);
}
