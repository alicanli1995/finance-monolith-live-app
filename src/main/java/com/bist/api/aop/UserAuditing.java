package com.bist.api.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserAuditing implements AuditorAware<String > {

    @Override
    public Optional<String> getCurrentAuditor() {
        var username = "anonymous";
        return Optional.of(username);
    }
}
