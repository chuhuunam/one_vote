package com.example.one_vote_service.config.persitence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {
    @Bean
    AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
