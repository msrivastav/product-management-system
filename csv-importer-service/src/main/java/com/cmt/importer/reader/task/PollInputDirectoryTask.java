package com.cmt.importer.reader.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * This task will do the following stuff
 * 1) Read the file from input directory
 * 2) Assign the file to worker thread
 * @author Manoo.Srivastav
 */
@Slf4j
@Component("pollInputDirectoryTask")
public class PollInputDirectoryTask implements Runnable, BaseTask {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	@Qualifier("threadPoolExecutor")
	ExecutorService workerThreadPool;
	
	@Value("${file.input.path}")
	private String inputFilePath;
	
	@Override
	public void run() {
		doTask();
	}

	@Override
	public void doTask() {
		log.info("Checking input file directory for CSV file : " + inputFilePath);
		File folder = new File(inputFilePath);
		//If folder doesn't exist then creating it
		if(!folder.exists()) {
			log.warn("Input folder {} doesn't exist. Createing it now.", folder.getAbsolutePath());
			if(folder.mkdirs()) {
				log.info("Input folder structure created successfully : {}", folder.getAbsolutePath());
			}
			else {
				log.error("Failed to create folder structure : {}",folder.getAbsolutePath());
				throw new RuntimeException("Failed to create folder structure : " + folder.getAbsolutePath());
			}
		}
		Predicate<File> fileNamePredicate = f -> {
			String fName = f.getName();
			if(fName.endsWith(".csv") || fName.endsWith(".CSV")) return true;
			else return false;
		};
		//Fetching all csv file found on the input path and assigning them to worker threads
		Arrays.stream(folder.listFiles()) //Listing all files in the folder
			  .filter(File::isFile)		  //Filtering only files
			  .filter(fileNamePredicate)  //Filtering csv files
			  .peek(t->log.info("File found : " + t))
			  .forEach(t->{
				  			Path p = Paths.get(t.getAbsolutePath());
				  			Path k = p;
				  			long processingTime = System.nanoTime();
				  			try {
								k = Files.move(p, p.resolveSibling(t.getName()+"_DONE_"+processingTime)); //Renaming the file before processing so that in the next processing of poller, same file is not picked up
							} catch (IOException e) {									  // before it is moved
								e.printStackTrace();
							}
				  			ReadCsvFileTask readCsvFileTask = (ReadCsvFileTask)context.getBean(ReadCsvFileTask.class);
				  			readCsvFileTask.setFile(k.toFile());
				  			workerThreadPool.execute(readCsvFileTask);
				  			});
	}
}
