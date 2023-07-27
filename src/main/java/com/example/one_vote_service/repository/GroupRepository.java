package com.example.one_vote_service.repository;

import com.example.one_vote_service.domain.entity.auth.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByStatusTrue();
}
