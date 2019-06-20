package com.cmt.qreader.cache.configuration;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides a cache for object that are just received from the queue.
 * For a certain period of time, if the request of processing of same object comes from the queue,
 * then to reduce the number of calls made to DB service, following actions will happen:
 * 1) Request will be checked against cache (lets call it request cache)
 * 2) If same request is there in cache, then db service will not be called
 * 3) After successful call to DB service, request will be added to request cache
 * 
 * This process is not universally fault proof, but it will reduce the number of unnecessary calls to DB service for a short period of time
 * 
 * @author Manoo.Srivastav
 */
@Configuration
public class CacheConfiguration {

	@Value("${cache.name}")
	private String cacheName;
	
	@Value("${cache.capacity}")
	private Long maxCacheCapacity;
	
	@Value("${cache.idle.time}")
	private long timeToIdle;
	
	@Value("${cache.live.time}")
	private long timeTolive;
	
	@Bean("productRequestCache")
	public Cache<String, String> cache() {
	    
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        
	    CacheConfigurationBuilder<String, String> configBuilder = 
	    		CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, 
	    											  				   ResourcePoolsBuilder.heap(maxCacheCapacity).build())		//Max capacity of cache
	    								 .withExpiry(Expirations.timeToIdleExpiration(Duration.of(timeToIdle,TimeUnit.SECONDS)))//Idle time of object	    								 
					  					 .withExpiry(Expirations.timeToLiveExpiration(Duration.of(timeTolive,TimeUnit.SECONDS)));//Time to live of object
	    								 						  							
	    cacheManager.createCache(cacheName, configBuilder);
		
	    return cacheManager.getCache(cacheName, String.class, String.class);

	}	
}
