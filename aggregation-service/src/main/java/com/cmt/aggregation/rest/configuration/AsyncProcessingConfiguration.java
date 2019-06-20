package com.cmt.aggregation.rest.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import com.cmt.aggregation.rest.exceptions.RequestProcessingException;

/**
 * This class provides thread configuration for async processing
 * @author Manoo.Srivastav
 */
@Configuration
@EnableAsync
public class AsyncProcessingConfiguration implements AsyncConfigurer {

	@Value("${async.thread.max}")
	private int maxWorkerThreadCount;
	/*
	 * This is thread pool of worker threads that does request processing
	 * I have used ThreadPoolExecutor because this implementation gives me freedom to choose max threads.
	 */
	@Override
	public Executor getAsyncExecutor() {
		return new ThreadPoolExecutor(1, maxWorkerThreadCount, 10, TimeUnit.SECONDS, new LinkedTransferQueue<Runnable>());
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new RequestProcessingException();
	}
}
