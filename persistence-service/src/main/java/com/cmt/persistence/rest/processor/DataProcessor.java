package com.cmt.persistence.rest.processor;

import java.util.List;

/**
 * Interface to define basic CRUD and readAll operation for any implementing processor
 * @author Manoo.Srivastav
 * @param <T>
 */
public interface DataProcessor<R,T,K> {
	
	public R create(T t); 
	
	public R update(T t);
	
	public void delete(K k);
	
	public R read(K k);
	
	public List<? extends R> readAll();
}
