package com.interdiscount.demo.domain;

import static java.util.Objects.nonNull;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.interdiscount.demo.DemoApplicationProperties;

class AuditorAwareImpl implements AuditorAware<String> {

    private final DemoApplicationProperties properties;

    AuditorAwareImpl(DemoApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public Optional<String> getCurrentAuditor() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        String user = null;

        if (nonNull(securityContext) && nonNull(securityContext.getAuthentication())) {
          user = ((OidcUser)securityContext.getAuthentication().getPrincipal()).getSubject();
        }

        if (Objects.isNull(user)) {
            user = this.properties.getAdminId();
        }

        return Optional.of(user);
    }
}