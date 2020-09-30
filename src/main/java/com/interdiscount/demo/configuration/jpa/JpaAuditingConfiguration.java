package com.interdiscount.demo.configuration.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.interdiscount.demo.DemoApplicationProperties;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider(DemoApplicationProperties properties) {
        return new AuditorAwareImpl(properties);
    }

    
}