package com.example.one_vote_service.repository;

import com.example.one_vote_service.domain.entity.auth.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

}
