package com.beyond.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {


	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}