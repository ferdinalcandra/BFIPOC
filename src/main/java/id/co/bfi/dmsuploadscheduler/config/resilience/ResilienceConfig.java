package id.co.bfi.dmsuploadscheduler.config.resilience;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.retry.RetryConfig;

@Configuration
public class ResilienceConfig {
	
	@Bean
	public RetryConfig retryConfig() {
		return RetryConfig.custom().
		maxAttempts(3000).waitDuration(Duration.ofSeconds(10)).build();
		
	}
}
