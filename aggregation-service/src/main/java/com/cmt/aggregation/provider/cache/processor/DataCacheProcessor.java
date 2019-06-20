package com.cmt.aggregation.provider.cache.processor;

import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cmt.aggregation.rest.model.request.Product;

import lombok.extern.slf4j.Slf4j;

/**
 * This cache is used to store product request data
 * Key is UUID of product and Value is Product data
 * @author Manoo.Srivastav
 *
 */
@Component("dataCacheProcessor")
@Slf4j
public class DataCacheProcessor implements BaseCacheProcessor<String, Product> {

	@Autowired
	@Qualifier("dataCache")
	private Cache<String, Product> dataCache;
	
	@Override
	public void addToCache(String key, Product value) {
		log.info("Putting the key {} into data cache", key);
		dataCache.put(key, value);
	}

	@Override
	public void removeFromCache(String key) {
		log.info("Removing key {} from data cache", key);
		dataCache.remove(key);
	}

	@Override
	public boolean isInCache(String key) {
		boolean result = dataCache.containsKey(key);
		log.info("Key {} is in data cache : {}", key, result);
		return result;
	}

	@Override
	public Product getFromCache(String key) {
		Product value = dataCache.get(key);
		log.info("Value found in data cache for key {} : {}", key, value);
		return value;
	}

}
