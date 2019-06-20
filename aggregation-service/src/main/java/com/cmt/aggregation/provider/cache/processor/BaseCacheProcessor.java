package com.cmt.aggregation.provider.cache.processor;

/**
 * This interface is used to define common cache related operation for all caches
 * @author Manoo.Srivastav
 *
 */
public interface BaseCacheProcessor<K, V> {
	
	public V getFromCache(K key);
	
	public void addToCache(K key, V value);
	
	public void removeFromCache(K key);
	
	public boolean isInCache(K key);
}
