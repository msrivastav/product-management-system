package com.cmt.importer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:app.config")
public class CsvImporterServiceApplication {

	public static void main(String[] args) { //This is not a web application 
		new SpringApplicationBuilder(CsvImporterServiceApplication.class).web(WebApplicationType.NONE).run(args);
	}

}
