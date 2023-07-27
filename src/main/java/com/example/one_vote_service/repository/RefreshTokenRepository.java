package com.example.one_vote_service.repository;

import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.domain.entity.auth.RefreshToken;
import com.example.one_vote_service.domain.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUserAndChannel(User user, Channel channel);

}
