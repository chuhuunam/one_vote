package com.example.one_vote_service.repository;

import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.domain.entity.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByTokenAndUserIdAndChannelAndLogoutFalse(String jwt, Long userId, Channel channel);

    Token findByTokenAndUserIdAndLogoutFalse(String jwt, Long userId);

}
