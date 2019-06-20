package com.cmt.aggregation.provider.persistence.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * This provider is to get configuration related to persistence service
 * @author Manoo.Srivastav
 *
 */
@Configuration
public class PersistenceConfiguratonProvider {
	
	@Bean("simpleRestTemplate")
	@Primary
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
