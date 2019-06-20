package com.cmt.qreader.provicer.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * This class provides configuration for service service providers
 * @author Manoo.Srivastav
 */
@Configuration
public class ServiceProviderConfiguration {

	@Bean("simpleRestTemplate")
	@Primary
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
