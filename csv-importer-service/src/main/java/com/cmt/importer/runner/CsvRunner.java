package com.cmt.importer.runner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * This class starts a scheduled thread that keeps polling input folder and assigns found files to working threads
 * @author Manoo.Srivastav
 */
@Component
@Slf4j
public class CsvRunner  implements CommandLineRunner, BaseRunner {

	@Autowired
	@Qualifier("newSingleThreadScheduledExecutor")
	ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	@Qualifier("pollInputDirectoryTask")
	Runnable pollInputDirectoryTask;

	//Delay after each thread completes its execution
	@Value("${thread.polling.interval}")
	Integer scheduleDelay;

	@Value("${file.input.path}")
	String inputFilePath;
	
	@Value("${file.output.path}")
	String outputFilePath;

	@Override
	public void run(String... args) throws Exception {
		startRun();
	}

	@Override
	public void startRun() {
		log.info("Starting application execution");
		log.info("Starting input file polling thread for location : " + inputFilePath);
		
		ScheduledFuture<?> sf = scheduledExecutorService.scheduleWithFixedDelay(pollInputDirectoryTask, 0, scheduleDelay, TimeUnit.SECONDS);
		
		//logging the exception in thread execution 
		while(true) {
			try{
				sf.get();
			}
			catch(Exception e) {
				e.printStackTrace();
				log.error("Error in thread operation : " + e.getMessage());
				

			}
		}
	}

}
