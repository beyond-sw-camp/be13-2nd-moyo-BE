package com.beyond.backend.config;

import com.beyond.backend.domain.common.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {


	@Bean
	public AuditorAware<Long> auditorProvider() {
		return new AuditorAwareImpl();
	}
}