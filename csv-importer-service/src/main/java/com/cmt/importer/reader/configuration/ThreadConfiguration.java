package com.cmt.importer.reader.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfiguration {

	/*
	 * This single thread executor is created to take a task to keep checking the file folder for csv file and 
	 * put it on queue
	 */
	@Bean(name="newSingleThreadScheduledExecutor")
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newSingleThreadScheduledExecutor();
	}
	
	/*
	 * This is thread pool of worker threads that get file from scheduler thread
	 */
	@Bean(name="threadPoolExecutor")
	public ExecutorService threadPoolExecutor() {
		return Executors.newCachedThreadPool();
	}
}
