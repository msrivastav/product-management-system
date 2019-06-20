package com.cmt.aggregation.provider.cache.configuration;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cmt.aggregation.rest.model.request.Product;

/**
 * This class provides configuration for the cache
 * @author Manoo.Srivastav
 */
@Configuration
public class CacheConfigurationProvider {
	
	@Value("${data.cache.name}")
	private String dataCacheName;
	
	@Value("${data.cache.capacity}")
	private Long dataCacheCapacity;
	
	@Value("${data.cache.idle.time}")
	private long dataTimeToIdle;
	
	@Value("${data.cache.live.time}")
	private long dataTimeTolive;
	
	@Value("${data.cache.persistence.directory}")
	private String cacheDirectory;
	
	@Value("${data.cache.persistence.size}")
	private long cacheMemorySize;
	
	/**
	 * Data cache configuration - This is persistent cache and can survive a restart
	 * This cache stores data as value for request
	 * If async data fetch is failed, request will also be evicted from request cache
	 * @return
	 */
	@Bean("dataCache")
	public Cache<String, Product> dataCache() {
		
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(CacheManagerBuilder.persistence(cacheDirectory))
				.build();
        cacheManager.init();
        
	    CacheConfigurationBuilder<String, Product> configBuilder = 
	    		CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Product.class, 
	    											  				   ResourcePoolsBuilder.heap(dataCacheCapacity)						//Max heap capacity of cache
	    											  				   					   .disk(cacheMemorySize, MemoryUnit.MB, true)  // Max disk capacity of cache
	    											  				   					   .build())		
	    								 .withExpiry(Expirations.timeToIdleExpiration(Duration.of(dataTimeToIdle,TimeUnit.SECONDS)))	//Idle time of object	    								 
					  					 .withExpiry(Expirations.timeToLiveExpiration(Duration.of(dataTimeTolive,TimeUnit.SECONDS)));	//Time to live of object
	    								 						  							
	    cacheManager.createCache(dataCacheName, configBuilder);
		
	    return cacheManager.getCache(dataCacheName, String.class, Product.class);
	}
}
