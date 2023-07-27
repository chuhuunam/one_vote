package com.example.one_vote_service.repository;

import com.example.one_vote_service.domain.entity.auth.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findAllByStatusTrue();

    List<Page> findAllByStatusTrueAndIdNotIn(List<Long> Ids);

}
