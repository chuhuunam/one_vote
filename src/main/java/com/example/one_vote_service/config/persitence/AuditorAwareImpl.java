package com.example.one_vote_service.config.persitence;

import com.example.one_vote_service.config.security.AccountDetail;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(((AccountDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }
}
