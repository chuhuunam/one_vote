package com.example.one_vote_service.repository;

import com.example.one_vote_service.domain.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByIdAndStatusTrue(Long userId);

    User findByUsername(String username);

    User findByEmail(String email);
}
