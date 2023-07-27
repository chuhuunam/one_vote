package com.example.one_vote_service.repository;

import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.domain.entity.auth.UserFcmKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFcmKeyRepository extends JpaRepository<UserFcmKey, Long> {
    List<UserFcmKey> findAllByFcmKey(String fcmKey);

    UserFcmKey findByUserIdAndChannelAndToken(Long userId, Channel channel, String jwt);
}
