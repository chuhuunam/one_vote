package com.example.one_vote_service.service.impl;

import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.config.constant.MessageCode;
import com.example.one_vote_service.domain.entity.auth.RefreshToken;
import com.example.one_vote_service.exception.TokenRefreshException;
import com.example.one_vote_service.repository.RefreshTokenRepository;
import com.example.one_vote_service.repository.UserRepository;
import com.example.one_vote_service.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final long refreshTokenDurationMs = 30 * 24 * 60 * 60 * 1000L;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(Long userId, Channel channel) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setChannel(channel);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(MessageCode.REFRESH_TOKEN_NOT_EXIST);
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId, Channel channel) {
        refreshTokenRepository.deleteByUserAndChannel(userRepository.findById(userId).get(), channel);
    }
}
