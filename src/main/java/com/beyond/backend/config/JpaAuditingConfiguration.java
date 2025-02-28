package com.beyond.backend.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {


	//Auditing 설정~~~
	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
