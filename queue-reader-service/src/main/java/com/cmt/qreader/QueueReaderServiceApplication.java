package com.cmt.qreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@SpringBootApplication
@PropertySource(value = "classpath:app.config")
public class QueueReaderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueReaderServiceApplication.class, args);
	}

}
