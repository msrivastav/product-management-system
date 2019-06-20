package com.cmt.importer.reader.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmt.importer.writer.sender.BaseMessageSender;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pojo.Product;

/**
 * This is worker thread that will do following things:
 * 1) Parse CSV file
 * 2) Send date to Queue
 * 3) Move file to output directory
 * @author Manoo.Srivastav
 */
@Slf4j
@Component("readCsvFileTask")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReadCsvFileTask implements Runnable, BaseTask {
	
	@Autowired
	@Qualifier("queueMessageSender")
	private BaseMessageSender queueMessageSender;
	
	@Value("${file.output.path}")
	private String outputFliePath;
	
	@Setter
	private File file;
	
	@Override
	public void run() {
		doTask();
	}
	
	@Override
	public void doTask() {
		log.info("Parsing file : " + file);
		//Starting the process of reading file
		try(FileInputStream fin = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin, Charset.defaultCharset()));) {
			
			//Creating a lambda that maps string record to object
			Function<String,Product> stringToProduct = t->{
				try{ 
					String[] arr = t.split(",");
					return Product.builder()
								  .uuid(arr[0])
								  .name(arr[1])
								  .description(arr[2])
								  .provider(arr[3])
								  .isAvailable(Boolean.parseBoolean(arr[4]))
								  .measurementUnit(arr[5]).build(); 
				
				}
				catch(Exception e) {
					log.error("Error while parsing {} : {}", t, e.getMessage());
				}
				return null;
			};
			
			//Processing file with parallel streams
			br.lines()
			.skip(1)					//Skipping first line with headers
			.parallel() 				//Converting to parallel stream for faster processing
			.map(stringToProduct)		//Mapping string to product
			.filter(Objects::nonNull)	//Filtering unparsed rows
			.forEach(queueMessageSender::sendMsg);

			
		}
		catch (Exception e) {
			log.error("Error while processing the file : " + file.getName());
		}
		
		//moving the file to output folder after processing
		try {
			File folder = new File(outputFliePath);
			if(!folder.exists()) {
				log.warn("Output folder {} doesn't exist. Createing it now.", folder.getAbsolutePath());
				if(folder.mkdirs()) {
					log.info("Output folder structure created successfully : {}", folder.getAbsolutePath());
				}
				else {
					log.error("Failed to create folder structure : {}",folder.getAbsolutePath());
					throw new RuntimeException("Failed to create folder structure : " + folder.getAbsolutePath());
				}
			}
			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(outputFliePath+"/"+file.getName().substring(0, file.getName().length()-5)));
		} 
		catch (IOException e) {
			e.printStackTrace();
			log.error("Error while moving the file : " + file.getName());
		}
	}



}
